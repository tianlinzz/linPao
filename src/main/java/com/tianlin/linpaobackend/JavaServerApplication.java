package com.tianlin.linpaobackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@MapperScan("com.tianlin.linpaobackend.mapper") // 扫描 Mapper, 生成 Mapper 对象 传入的字符串要和 Mapper 的包的路径一致
@EnableWebMvc
public class JavaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaServerApplication.class, args);
    }

}
