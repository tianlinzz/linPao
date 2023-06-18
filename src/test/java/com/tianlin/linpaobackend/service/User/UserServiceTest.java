package com.tianlin.linpaobackend.service.User;

import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

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
}