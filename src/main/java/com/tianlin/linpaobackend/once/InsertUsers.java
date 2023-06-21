package com.tianlin.linpaobackend.once;

import com.tianlin.linpaobackend.mapper.UserMapper;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component // 将该类交给 Spring 管理, 变成一个 Bean
public class InsertUsers {

    @Resource
    private UserService userService;

    /**
     * 批量插入用户
     */
//    @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE) // 每隔 5s 执行一次, 但是只执行一次。fixedRate = Long.MAX_VALUE 表示执行一次
    public void batchInsertUsers() { // 同步方法
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int INSERT_USER_COUNT = 1000;  //  插入用户的数量
        // 创建用户列表
        List<User> userList = new ArrayList<>();
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
            userList.add(user);
        }
        userService.saveBatch(userList, 100); // 批量插入用户, 每次插入 100 条
        stopWatch.stop();
        System.out.println("插入" + INSERT_USER_COUNT + "条用户数据, 耗时: " + stopWatch.getTotalTimeMillis() + "ms");
    }

    /**
     * 批量插入用户,异步
     */
//    @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void asynchronousBatchInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int INSERT_USER_COUNT = 100000;  //  插入用户的数量
        int BATCH_INSERT_COUNT = 5000; // 每次插入的数量
        int BATCH_INSERT_TIMES = INSERT_USER_COUNT / BATCH_INSERT_COUNT; // 插入的次数

        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for(int i = 0; i < BATCH_INSERT_TIMES; i++) {
            // 创建用户列表
            List<User> userList = new ArrayList<>();
            do {
                j++;
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
                userList.add(user);
            } while (j % 10000 != 0);
            // 异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                userService.saveBatch(userList, BATCH_INSERT_COUNT); // 批量插入用户
            });
            futureList.add(future);
        }

        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();

        stopWatch.stop();
        System.out.println("插入" + INSERT_USER_COUNT + "条用户数据, 耗时: " + stopWatch.getTotalTimeMillis() + "ms");
    }
}
