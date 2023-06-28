package com.tianlin.linpaobackend.service.User;


import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void test() {

        RList<Object> rList = redissonClient.getList("redisson-list");
//        rList.add("a");
         System.out.print(rList.get(0));
    }
}
