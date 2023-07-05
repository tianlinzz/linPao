package com.tianlin.linpaobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianlin.linpaobackend.model.domain.Team;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.model.dto.TeamQuery;
import com.tianlin.linpaobackend.model.request.TeamDissolveRequest;
import com.tianlin.linpaobackend.model.request.TeamJoinRequest;
import com.tianlin.linpaobackend.model.request.TeamQuitRequest;
import com.tianlin.linpaobackend.model.request.TeamUpdateRequest;
import com.tianlin.linpaobackend.model.response.PageResponse;
import com.tianlin.linpaobackend.model.response.TeamInfoResponse;
import com.tianlin.linpaobackend.model.vo.TeamUserVO;

import java.util.List;

/**
 * @author 张添琳
 * {@code @description} 针对表【team(队伍表)】的数据库操作Service
 * {@code @createDate} 2023-06-28 14:29:09
 */
public interface TeamService extends IService<Team> {

    /**
     * @param team      队伍信息
     * @param loginUser 当前登录用户
     */
    long createTeam(Team team, User loginUser);

    /**
     * @param teamId 队伍id
     * @return 返回队伍信息
     */
    TeamInfoResponse getTeamById(long teamId);

    /**
     * @param teamQuery 查询条件
     * @return 返回队伍列表
     */
    List<TeamUserVO> getTeamList(TeamQuery teamQuery, boolean idAdmin);

    /**
     * 分页查询队伍列表
     *
     * @param teamQuery 查询条件
     * @return 返回队伍列表
     */
    PageResponse<TeamUserVO> getTeamListByPage(TeamQuery teamQuery, boolean isAdmin);

    /**
     * @param TeamUpdateRequest 队伍修改的信息
     * @param isAdmin           是否是管理员
     * @param loginUserId       当前登录用户id
     * @return 返回队伍信息
     */
    boolean updateTeam(TeamUpdateRequest TeamUpdateRequest, boolean isAdmin, long loginUserId);

    /**
     * @param teamDissolveRequest 队伍解散的信息
     * @param isAdmin             是否是管理员
     * @param loginUserId         当前登录用户id
     * @return 返回是否删除成功
     */
    boolean dissolveTeam(TeamDissolveRequest teamDissolveRequest, boolean isAdmin, long loginUserId);

    /**
     * @param teamJoinRequest 队伍加入请求
     * @param loginUserId     当前登录用户id
     * @return 返回是否加入成功
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, long loginUserId);

    /**
     * @param teamQuitRequest 队伍退出请求
     * @param loginUserId 当前登录用户id
     * @return 返回是否退出成功
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, long loginUserId);
}
