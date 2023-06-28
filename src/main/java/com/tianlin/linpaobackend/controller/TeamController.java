package com.tianlin.linpaobackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianlin.linpaobackend.common.BaseResponse;
import com.tianlin.linpaobackend.common.ErrorCode;
import com.tianlin.linpaobackend.common.ResultUtils;
import com.tianlin.linpaobackend.exception.BusinessException;
import com.tianlin.linpaobackend.model.domain.Team;
import com.tianlin.linpaobackend.model.dto.TeamQuery;
import com.tianlin.linpaobackend.model.request.PageRequest;
import com.tianlin.linpaobackend.service.TeamService;
import com.tianlin.linpaobackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.save(team);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加队伍失败");
        }
        return ResultUtils.success(team.getId());
    }

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

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateById(team);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍失败");
        }
        return ResultUtils.success(true);
    }

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

    @GetMapping("list")
    public BaseResponse<List<Team>> listTeam(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamQuery);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<Team> result = teamService.list(queryWrapper);
        return ResultUtils.success(result);
    }

    @GetMapping("/list/page/{size}/{num}")
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
