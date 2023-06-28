package com.tianlin.linpaobackend.config;


import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置类
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;
    private int port;
    @Bean
    public RedissonClient redissonClient() {
        // 1. Create config object
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        // 2. Set single server config
        config.useSingleServer().setAddress(redisAddress).setDatabase(2);
        // 3. Create Redisson instance and return it
        return Redisson.create(config);
    }
}
