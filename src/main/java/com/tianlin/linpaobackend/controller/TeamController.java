package com.tianlin.linpaobackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianlin.linpaobackend.common.BaseResponse;
import com.tianlin.linpaobackend.common.ErrorCode;
import com.tianlin.linpaobackend.common.ResultUtils;
import com.tianlin.linpaobackend.exception.BusinessException;
import com.tianlin.linpaobackend.model.domain.Team;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.model.dto.TeamQuery;
import com.tianlin.linpaobackend.model.request.PageRequest;
import com.tianlin.linpaobackend.model.request.TeamAddRequest;
import com.tianlin.linpaobackend.model.request.TeamUpdateRequest;
import com.tianlin.linpaobackend.model.vo.TeamUserVO;
import com.tianlin.linpaobackend.service.TeamService;
import com.tianlin.linpaobackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.tianlin.linpaobackend.constant.UserConstant.USER_LOGIN_STATUS;


/**
 * 队伍相关接口
 * @author 张添琳
 */
@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;

    /**
     * @param teamAddRequest 队伍信息
     * @return 返回创建的队伍id
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamAddRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        long result = teamService.createTeam(team, loginUser);
        return ResultUtils.success(result);
    }


    /**
     * @param id 队伍id
     * @return 返回删除结果
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * @param teamUpdateRequest 队伍修改的信息
     * @param request 请求
     * @return 返回更新结果
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean isAdmin = userService.isAdmin(request);
        long loginUserId = loginUser.getId();
        boolean result = teamService.updateTeam(teamUpdateRequest, isAdmin, loginUserId);
        return ResultUtils.success(result);
    }

    /**
     * @param id 队伍id
     * @return 返回队伍信息
     */
    @GetMapping("/get")
    public BaseResponse<Team> getTeam(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取队伍失败");
        }
        return ResultUtils.success(team);
    }

    /**
     * @param teamQuery 队伍查询条件
     * @return 返回队伍列表
     */
    @GetMapping("list")
    public BaseResponse<List<TeamUserVO>> listTeam(TeamQuery teamQuery, HttpServletRequest request) {
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> result = teamService.getTeamList(teamQuery, isAdmin);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询队伍
     * @param teamQuery 队伍查询条件
     * @return 返回队伍列表
     */
    @GetMapping("/list/page")
    public BaseResponse<PageRequest> listTeamByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamQuery);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> result = teamService.page(page, queryWrapper);
        return ResultUtils.success(new PageRequest(result.getTotal(), result.getRecords()));
    }
}
