package com.tianlin.linpaobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianlin.linpaobackend.model.domain.Team;
import com.tianlin.linpaobackend.service.TeamService;
import com.tianlin.linpaobackend.mapper.TeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 张添琳
* @description 针对表【team(队伍表)】的数据库操作Service实现
* @createDate 2023-06-28 14:29:09
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




