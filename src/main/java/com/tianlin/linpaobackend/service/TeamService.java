package com.tianlin.linpaobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianlin.linpaobackend.model.domain.Team;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.model.dto.TeamQuery;
import com.tianlin.linpaobackend.model.vo.TeamUserVO;

import java.util.List;

/**
* @author 张添琳
* @description 针对表【team(队伍表)】的数据库操作Service
* @createDate 2023-06-28 14:29:09
*/
public interface TeamService extends IService<Team> {

    /**
     * @param team 队伍信息
     * @param loginUser 当前登录用户
     */
   long createTeam(Team team, User loginUser);

    /**
     * @param teamQuery 查询条件
     * @return 返回队伍列表
     */
   List<TeamUserVO> getTeamList(TeamQuery teamQuery, boolean idAdmin);
}
