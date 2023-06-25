package com.tianlin.linpaobackend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 * 1. 缓存预热
 *
 * @author 张添琳
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    // 重点用户id，才去缓存预热
    private final List<Long> mainIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

    /**
     * 缓存预热
     * 每天0点执行
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void preCache() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        for (Long userId : mainIds) {
            String redisKey = String.format("linpao:user:recommend:%s", userId);

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1, 10), queryWrapper);
            try {
                // 设置缓存，过期时间为一天
                valueOperations.set(redisKey, userPage, 1, TimeUnit.DAYS);
            }catch (Exception e) {
                log.error("redis set key error:{}", e.getMessage());
            }
        }
    }
}
