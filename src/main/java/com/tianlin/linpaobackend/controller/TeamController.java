package com.tianlin.linpaobackend.controller;

import com.tianlin.linpaobackend.common.BaseResponse;
import com.tianlin.linpaobackend.common.ErrorCode;
import com.tianlin.linpaobackend.common.ResultUtils;
import com.tianlin.linpaobackend.exception.BusinessException;
import com.tianlin.linpaobackend.model.domain.Team;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.model.dto.TeamQuery;
import com.tianlin.linpaobackend.model.request.*;
import com.tianlin.linpaobackend.model.response.PageResponse;
import com.tianlin.linpaobackend.model.response.TeamInfoResponse;
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
    @PostMapping("/create")
    public BaseResponse<Long> createTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
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
     * @param teamDissolveRequest 队伍解散的信息
     * @return 返回删除结果
     */
    @PostMapping("/dissolve")
    public BaseResponse<Boolean> dissolveTeam(@RequestBody TeamDissolveRequest teamDissolveRequest, HttpServletRequest request) {
        if (teamDissolveRequest == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean isAdmin = userService.isAdmin(request);
        long loginUserId = loginUser.getId();
        boolean result = teamService.dissolveTeam(teamDissolveRequest, isAdmin, loginUserId);
        return ResultUtils.success(result);
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
    public BaseResponse<TeamInfoResponse> getTeam(@RequestParam long id) {
        TeamInfoResponse result = teamService.getTeamById(id);
        return ResultUtils.success(result);
    }

    /**
     * @param teamQuery 队伍查询条件
     * @return 返回队伍列表
     */
    @GetMapping("list")
    public BaseResponse<List<TeamUserVO>> listTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
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
    public BaseResponse<PageResponse<TeamUserVO>> listTeamByPage(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean isAdmin = userService.isAdmin(request);
        PageResponse<TeamUserVO> pageResponse = teamService.getTeamListByPage(teamQuery, isAdmin);
        return ResultUtils.success(pageResponse);
    }

    /**
     * @param teamJoinRequest 队伍加入信息
     * @param request 请求
     * @return 返回加入结果
     */
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long loginUserId = loginUser.getId();
        boolean result = teamService.joinTeam(teamJoinRequest, loginUserId);
        return ResultUtils.success(result);
    }

    /**
     * @param teamQuitRequest 队伍id
     * @param request 请求
     * @return 返回退出结果
     */
    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long loginUserId = loginUser.getId();
        boolean result = teamService.quitTeam(teamQuitRequest, loginUserId);
        return ResultUtils.success(result);
    }
}
