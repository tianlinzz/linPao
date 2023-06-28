package com.tianlin.linpaobackend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.tianlin.linpaobackend.constant.UserConstant.REDIS_USER_PREFIX;

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

    @Resource
    private RedissonClient redissonClient;


    // 重点用户id，才去缓存预热
    private final List<Long> mainIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

    /**
     * 缓存预热
     * 每天0点执行
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void preCache() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String lockKey = String.format("%s:preCache:lock", REDIS_USER_PREFIX);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 只有一个线程能拿到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) { // -1表示会自动续期，续期时间为30s
                for (Long userId : mainIds) {
                    String redisKey = String.format("%s:recommend:%s", REDIS_USER_PREFIX, userId);
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
        } catch (InterruptedException e) {
            log.error("get lock error:{}", e.getMessage());
        } finally {
            // 释放锁, 防止死锁
            if (lock.isHeldByCurrentThread()) { // 只有当前线程持有锁时才释放
                lock.unlock();
            }
        }
    }
}
