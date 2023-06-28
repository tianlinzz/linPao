package com.tianlin.linpaobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianlin.linpaobackend.model.domain.UserTeam;
import com.tianlin.linpaobackend.service.UserTeamService;
import com.tianlin.linpaobackend.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 张添琳
* @description 针对表【user_team(用户队伍关系表)】的数据库操作Service实现
* @createDate 2023-06-28 14:30:19
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




