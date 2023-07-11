package com.tianlin.linpaobackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                //当**Credentials为true时，**Origin不能为星号，需为具体的ip地址【如果接口不带cookie,ip无需设成具体ip】
                .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:8000")
                // 允许跨域带上cookies
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                //跨域允许头部设置
                .allowedHeaders("*")
                //跨域允许时间
                .maxAge(3600);
    }
}