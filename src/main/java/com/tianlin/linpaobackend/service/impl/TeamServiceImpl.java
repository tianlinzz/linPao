package com.tianlin.linpaobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianlin.linpaobackend.common.ErrorCode;
import com.tianlin.linpaobackend.exception.BusinessException;
import com.tianlin.linpaobackend.mapper.TeamMapper;
import com.tianlin.linpaobackend.model.domain.Team;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.model.domain.UserTeam;
import com.tianlin.linpaobackend.model.dto.TeamQuery;
import com.tianlin.linpaobackend.model.enums.TeamStatus;
import com.tianlin.linpaobackend.model.vo.TeamUserVO;
import com.tianlin.linpaobackend.model.vo.UserVO;
import com.tianlin.linpaobackend.service.TeamService;
import com.tianlin.linpaobackend.service.UserService;
import com.tianlin.linpaobackend.service.UserTeamService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author 张添琳
* @description 针对表【team(队伍表)】的数据库操作Service实现
* @createDate 2023-06-28 14:29:09
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    /**
     * @param team      队伍信息
     * @param loginUser 当前登录用户
     * @return  返回创建的队伍id
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务回滚
    public long createTeam(Team team, User loginUser) {
        //  请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //  是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //  校验信息
        // 1、队伍人数 > 1 且 <= 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不合法");
        }
        //  2、队伍标题 <= 20
        String name = Optional.ofNullable(team.getName()).orElse("");
        if (name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不合法");
        }
        //  描述 <= 512
        String description = Optional.ofNullable(team.getDescription()).orElse("");
        if (description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不合法");
        }
        //  status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatus teamStatus = TeamStatus.getTeamStatus(status);
        if (teamStatus == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不合法");
        }
        //  如果 status 是加密状态，一定要有密码，且密码 <= 8
        if (teamStatus.equals(TeamStatus.SELECT)) {
            String password = Optional.ofNullable(team.getPassword()).orElse("");
            if (password.length() == 0 || password.length() > 8) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码不合法");
            }
        }
        //  超时时间 > 当前时间
        Date timeout = Optional.ofNullable(team.getExpireTime()).orElse(new Date());
        if (timeout.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        //  校验用户最多创建 5 个队伍
        // todo 用户可能在同一时间创建多个队伍，需要加锁
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long count = this.count(queryWrapper);
        if (count >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍数量超过限制");
        }
        //  插入队伍信息到队伍表
        team.setUserId(userId);
        boolean result = this.save(team);
        if (!result) { // 插入失败，派出异常，事务就会自动回滚
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍创建失败");
        }
        //  插入用户 => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(team.getId());
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) { // 插入失败，派出异常，事务就会自动回滚
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍创建失败");
        }
        return team.getId();
    }

    /**
     * @param teamQuery 查询条件
     * @return 返回队伍列表
     */
    @Override
    public List<TeamUserVO> getTeamList(TeamQuery teamQuery, boolean idAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        // 1、根据条件查询队伍列表
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id >= 1) {
                queryWrapper.eq("id", id);
            }
            Long  userId = teamQuery.getUserId();
            if (userId != null && userId >= 1) {
                queryWrapper.eq("userId", userId);
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name) && name.length() > 0) {
                queryWrapper.like("name", name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description) && description.length() > 0) {
                queryWrapper.like("description", description);
            }
            Integer status = teamQuery.getStatus();
            TeamStatus teamStatus = TeamStatus.getTeamStatus(status);
            if (teamStatus == null) {
                teamStatus = TeamStatus.PUBLIC;
            }
            if (!idAdmin && !teamStatus.equals(TeamStatus.PUBLIC)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            queryWrapper.eq("status", teamStatus.getCode());
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum >= 1) {
                queryWrapper.eq("maxNum", maxNum);
            }
            String keyword = teamQuery.getKeyword();
            if (StringUtils.isNotBlank(keyword) && keyword.length() > 0) {
                queryWrapper.and(wrapper -> wrapper.like("name", keyword).or().like("description", keyword));
            }
        }
        // 不展示已经过期的队伍或者没有设置过期时间的队伍
        queryWrapper.and(wrapper -> wrapper.gt("expireTime", new Date()).or().isNull("expireTime"));
        // 2、查询队伍列表
        List<Team> teamList = this.list(queryWrapper);
        // 3、关联查询用户信息
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        // SQL
        // 查询队伍和创建人的信息
        // select * from team t left join user u on t.userId = ut.id
        // 查询队伍和已加入成员的id
        // select * from team t left join user_team ut on t.id = ut.teamId left join user u on ut.userId = u.id
        // 查询创建人的信息
        for (Team team : teamList ) {

            Long userId = team.getUserId(); // 队长id
            User createUser = userService.getById(userId);
            User safetUser = userService.getSafetUser(createUser);
            TeamUserVO teamUserVO = new TeamUserVO();
            UserVO userVO = new UserVO();
            if (safetUser != null) {
                try {
                    BeanUtils.copyProperties(teamUserVO, team);
                    BeanUtils.copyProperties(userVO, safetUser);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            QueryWrapper userTeamQueryWrapper = new QueryWrapper();
            // 查询队伍成员，并且不包含队长
            userTeamQueryWrapper.eq("teamId", team.getId());
            userTeamQueryWrapper.ne("userId", userId);
            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
            List<UserVO> userVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(userTeamList)) { // 有成员
                for (UserTeam userTeam : userTeamList) {
                    Long teamUserId = userTeam.getUserId();
                    User user = userService.getById(teamUserId);
                    User safeUser = userService.getSafetUser(user);
                    UserVO joinUser = new UserVO();
                    if (safeUser != null) {
                        try {
                            BeanUtils.copyProperties(joinUser, safeUser);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    userVOList.add(joinUser);
                }
            }
            teamUserVO.setCreateUser(userVO);
            teamUserVO.setUserList(userVOList);
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }
}




