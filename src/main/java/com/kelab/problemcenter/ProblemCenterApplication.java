package com.kelab.problemcenter;

import cn.wzy.verifyUtils.annotation.EnableVerify;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableVerify
@MapperScan(basePackages = "com.kelab.problemcenter.repo.dao")
public class ProblemCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProblemCenterApplication.class, args);
    }

}
