package com.tianlin.linpaobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianlin.linpaobackend.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 天琳
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2023-06-02 19:34:35
* @Entity com.tianlin.usercenter.model.domain.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {
}




