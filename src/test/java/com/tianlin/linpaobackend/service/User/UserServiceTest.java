package com.tianlin.linpaobackend.service.User;

import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void getUserByTag() {
        List<String> tagNmaeList = Arrays.asList("java", "python");
        List<User> userList = userService.getUserByTag(tagNmaeList);
        Assertions.assertNotNull(userList);
    }

    @Test
    void getSafetUser() {
    }

    @Test
    void userRegister() {
    }

    @Test
    void userLogin() {
    }

    @Test
    void userLogout() {
    }

    @Test
    void checkUserInfo() {
    }

    @Test
    void testRedis() {
        ValueOperations valueOperations = redisTemplate.opsForValue(); // 操作字符串
        ListOperations listOperations = redisTemplate.opsForList(); // 操作列表
        valueOperations.set("test", "test");
        Assertions.assertEquals("test", valueOperations.get("test"));
    }
}