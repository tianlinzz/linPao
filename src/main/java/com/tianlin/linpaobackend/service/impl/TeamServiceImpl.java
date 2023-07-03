package com.tianlin.linpaobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianlin.linpaobackend.common.ErrorCode;
import com.tianlin.linpaobackend.exception.BusinessException;
import com.tianlin.linpaobackend.mapper.TeamMapper;
import com.tianlin.linpaobackend.model.domain.Team;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.model.domain.UserTeam;
import com.tianlin.linpaobackend.model.dto.TeamQuery;
import com.tianlin.linpaobackend.model.enums.TeamStatus;
import com.tianlin.linpaobackend.model.request.TeamDissolveRequest;
import com.tianlin.linpaobackend.model.request.TeamJoinRequest;
import com.tianlin.linpaobackend.model.request.TeamQuitRequest;
import com.tianlin.linpaobackend.model.request.TeamUpdateRequest;
import com.tianlin.linpaobackend.model.response.PageResponse;
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
* {@code @description} 针对表【team(队伍表)】的数据库操作Service实现
* {@code @createDate} 2023-06-28 14:29:09
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;


    /**
     * 根据搜索条件构建查询条件
     * @param teamQuery 队伍信息
     * @param isAdmin 是否为管理员
     * @return 查询条件
     */
    private QueryWrapper<Team> getQueryWrapper(TeamQuery teamQuery, boolean isAdmin) {
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
            if (!isAdmin && !teamStatus.equals(TeamStatus.PUBLIC)) {
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
        return queryWrapper;
    }

    /**
     * 根据队伍列表构建用户列表
     * @param teamList 队伍列表
     * @return 查询条件
     */
    private List<TeamUserVO> getUserVOListByTeamList(List<Team> teamList) {
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        // SQL
        // 查询队伍和创建人的信息
        // select * from team t left join user u on t.userId = ut.id
        // 查询队伍和已加入成员的id
        // select * from team t left join user_team ut on t.id = ut.teamId left join user u on ut.userId = u.id
        // 查询创建人的信息
        for (Team team : teamList) {
            Long userId = team.getUserId(); // 队长id
            // 查询队长信息
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
            // 查询队伍成员，并且不包含队长
            QueryWrapper userTeamQueryWrapper = new QueryWrapper();
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
    public List<TeamUserVO> getTeamList(TeamQuery teamQuery, boolean isAdmin) {
        // 1、根据查询条件构造查询条件
        QueryWrapper<Team> queryWrapper = getQueryWrapper(teamQuery, isAdmin);
        // 2、查询队伍列表
        List<Team> teamList = this.list(queryWrapper);
        // 3、关联查询用户信息
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        return getUserVOListByTeamList(teamList);
    }

    /**
     * 分页查询队伍列表
     * @param teamQuery 查询条件
     * @param isAdmin 是否是管理员
     * @return 返回队伍列表
     */
    @Override
    public PageResponse<TeamUserVO> getTeamListByPage(TeamQuery teamQuery, boolean isAdmin) {
        // 1、根据查询条件构造查询条件
        QueryWrapper<Team> queryWrapper = getQueryWrapper(teamQuery, isAdmin);
        // 2、分页查询队伍列表
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        IPage<Team> teamIPage = this.page(page, queryWrapper);
        List<Team> teamList = teamIPage.getRecords();
        long total = teamIPage.getTotal();
        PageResponse<TeamUserVO> pageResponse = new PageResponse<>();
        if (CollectionUtils.isEmpty(teamList)) {
            pageResponse.setTotal(total);
            pageResponse.setItems(new ArrayList<>());
            return pageResponse;
        }
        // 3、关联查询用户信息
        List<TeamUserVO> teamUserVOList = getUserVOListByTeamList(teamList);
        pageResponse.setTotal(total);
        pageResponse.setItems(teamUserVOList);
        return pageResponse;
    }

    /**
     * @param TeamUpdateRequest 队伍信息
     * @param idAdmin           是否是管理员
     * @param loginUserId       登录用户id
     * @return 返回是否修改成功
     */
    @Override
    public boolean updateTeam(TeamUpdateRequest TeamUpdateRequest, boolean idAdmin, long loginUserId) {
        Long id = TeamUpdateRequest.getId();
        if (id == null || id < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询队伍是否存在
        Team team = this.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        // 只有队长或者管理员才可以修改队伍信息
        if (!idAdmin && !team.getUserId().equals(loginUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 修改队伍信息
        try {
            BeanUtils.copyProperties(team, TeamUpdateRequest); // 将前端传递的参数拷贝到team对象中
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        // 只有加密的队伍才可以修改密码,如果是公开和私有的队伍，密码需要设置为空
        if (team.getStatus().equals(TeamStatus.SELECT.getCode())) {
            String password = TeamUpdateRequest.getPassword();
            if (StringUtils.isBlank(password)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码不能为空");
            }
            team.setPassword(password);
        } else {
            team.setPassword(null);
        }
        return this.updateById(team);
    }

    /**
     * 删除队伍
     *
     * @param teamDissolveRequest 队伍解散的信息
     * @param isAdmin             是否是管理员
     * @param loginUserId         当前登录用户id
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务回滚
    public boolean dissolveTeam(TeamDissolveRequest teamDissolveRequest, boolean isAdmin, long loginUserId) {
        // 查询队伍是否存在
        long teamId = teamDissolveRequest.getTeamId();
        if (teamId < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        // 只有队长或者管理员才可以删除队伍
        if (!isAdmin && !team.getUserId().equals(loginUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 删除队伍和队伍成员关联表
        QueryWrapper userTeamQueryWrapper = new QueryWrapper();
        userTeamQueryWrapper.eq("teamId", teamId);
        boolean removeResult = userTeamService.remove(userTeamQueryWrapper);
        boolean deleteResult = this.removeById(teamId);
        return removeResult && deleteResult;
    }

    /**
     * 加入队伍
     * @param teamJoinRequest  加入队伍请求参数
     * @param loginUserId 当前登录用户id
     * @return 是否加入成功
     */
    // todo 需要加锁，防止并发，因为同一时间可能有多个人加入同一个队伍，或者同一个发出多次加入队伍请求，导致队伍人数超过限制
    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, long loginUserId) {
        long teamId = teamJoinRequest.getTeamId();
        if (teamId < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询队伍是否存在
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        if (team.getUserId().equals(loginUserId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能加入自己创建的队伍");
        }
        // 查询队伍是否过期
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        // 禁止加入私密的队伍
        if (team.getStatus().equals(TeamStatus.PRIVATE.getCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "私密队伍禁止加入");
        }
        // 如果是加密的队伍，需要校验密码
        if (team.getStatus().equals(TeamStatus.SELECT.getCode())) {
            String password = teamJoinRequest.getPassword();
            if (StringUtils.isBlank(password)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码不能为空");
            }
            if (!password.equals(team.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码错误");
            }
        }
        // 查询用户是否已经加入队伍
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId", loginUserId);
        userTeamQueryWrapper.eq("teamId", teamId);
        UserTeam userTeam = userTeamService.getOne(userTeamQueryWrapper);
        if (userTeam != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已经加入队伍");
        }
        // 查询用户加入的队伍数量
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId", loginUserId);
        long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
        if (hasJoinNum > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "每个用户最多只能加入5个队伍");
        }
        // 查询队伍已经加入的人数
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId", teamId);
        long teamJoinNum = userTeamService.count(userTeamQueryWrapper);
        if (teamJoinNum >= team.getMaxNum()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数已满");
        }
        // 加入队伍
        UserTeam newUserTeam = new UserTeam();
        newUserTeam.setTeamId(teamId);
        newUserTeam.setUserId(loginUserId);
        newUserTeam.setJoinTime(new Date());
        return userTeamService.save(newUserTeam);
    }

    /**
     * 退出队伍
     * @param teamQuitRequest 退出队伍请求参数
     * @param loginUserId 当前登录用户id
     * @return 是否退出成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务回滚
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, long loginUserId) {
        long teamId = teamQuitRequest.getTeamId();
        if (teamId < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询队伍是否存在
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        // 查询用户是否已经加入队伍
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId", loginUserId);
        userTeamQueryWrapper.eq("teamId", teamId);
        UserTeam userTeam = userTeamService.getOne(userTeamQueryWrapper);
        if (userTeam == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未加入队伍");
        }
        // 队伍存在并且用户已经加入队伍
        // 1.如果队伍仅剩下队长一个人，队长退出队伍后，队伍自动解散
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId", teamId);
        long teamJoinNum = userTeamService.count(userTeamQueryWrapper);
        if (teamJoinNum == 1) {
            // 删除队伍和删除队伍下的所有人
            boolean quitResult = this.removeById(teamId);
            userTeamQueryWrapper = new QueryWrapper<>();
            userTeamQueryWrapper.eq("teamId", teamId);
            boolean removeResult = userTeamService.remove(userTeamQueryWrapper);
            return quitResult && removeResult;
        }

        // 2. 如果是队长退出队伍，需要将队伍的队长id顺延给第一个加入的人。并且退出队伍。
        if (team.getUserId().equals(loginUserId)) {
            // 查询队伍下的所有人
            userTeamQueryWrapper = new QueryWrapper<>();
            userTeamQueryWrapper.eq("teamId", teamId);
            userTeamQueryWrapper.last("order by id asc limit 2");
            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
            if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
            // 将队伍的队长id顺延给第一个加入的人
            UserTeam firstUserTeam = userTeamList.get(1);
            Team updateTeam = new Team();
            updateTeam.setId(teamId);
            updateTeam.setUserId(firstUserTeam.getUserId());
            boolean updateResult = this.updateById(updateTeam);
            // 退出队伍
            userTeamQueryWrapper = new QueryWrapper<>();
            userTeamQueryWrapper.eq("userId", loginUserId);
            userTeamQueryWrapper.eq("teamId", teamId);
            boolean removeResult = userTeamService.remove(userTeamQueryWrapper);
            return updateResult && removeResult;
        }

        // 3. 如果是队员退出队伍，直接删除队员
        return userTeamService.removeById(userTeam.getId());
    }
}




