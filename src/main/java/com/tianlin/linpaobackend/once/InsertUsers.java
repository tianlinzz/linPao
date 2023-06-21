package com.tianlin.linpaobackend.once;

import com.tianlin.linpaobackend.mapper.UserMapper;
import com.tianlin.linpaobackend.model.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

@Component // 将该类交给 Spring 管理, 变成一个 Bean
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

    /**
     * 批量插入用户
     */
    public void batchInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 插入用户的数量
        int INSERT_USER_COUNT = 1000;
        for (int i = 0; i < INSERT_USER_COUNT; i++) {
            User user = new User();
            user.setUsername("");
            user.setUserAccount("");
            user.setAvatarUrl("");
            user.setGender(0);
            user.setUserPassword("");
            user.setPhone("");
            user.setEmail("");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setUserCode(0L);
            user.setTags("");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println("插入" + INSERT_USER_COUNT + "条用户数据, 耗时: " + stopWatch.getTotalTimeMillis() + "ms");
    }
}
