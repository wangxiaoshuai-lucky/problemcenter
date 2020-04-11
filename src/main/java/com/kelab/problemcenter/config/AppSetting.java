package com.kelab.problemcenter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
public class AppSetting {

    public static Long cacheMillisecond;

    /**
     * 远端拉取判题数据密钥
     */
    public static String secretKey;

    @Value("${cache.millisecond}")
    public void setCacheMillisecond(Long cacheMillisecond) {
        AppSetting.cacheMillisecond = cacheMillisecond;
    }

    @Value("${key.secretKey}")
    public void setSecretKey(String secretKey) {
        AppSetting.secretKey = secretKey;
    }

}
