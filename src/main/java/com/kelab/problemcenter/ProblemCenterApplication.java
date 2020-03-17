package com.kelab.problemcenter;

import cn.wzy.verifyUtils.annotation.EnableVerify;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableVerify
@MapperScan(basePackages = "com.kelab.problemcenter.repo.dao")
@EnableFeignClients(basePackages = "com.kelab.problemcenter.support.facade")
public class ProblemCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProblemCenterApplication.class, args);
    }

}
