package com.kelab.problemcenter.dal.redis;

import com.kelab.problemcenter.config.AppSetting;
import com.kelab.problemcenter.constant.CacheConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class RedisCache {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public String get(CacheConstant bizName, String key) {
        return redisTemplate.opsForValue().get(bizName + key);
    }

    public void set(CacheConstant bizName, String key, String value) {
        try {
            redisTemplate.opsForValue().set(bizName + key, value, AppSetting.cacheMillisecond, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Object getAndSet(CacheConstant bizName, String key, String value) {
        String result;
        try {
            result = redisTemplate.opsForValue().getAndSet(bizName + key, value);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public boolean delete(CacheConstant bizName, String key) {
        boolean result = false;
        try {
            redisTemplate.delete(bizName + key);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void zAdd(CacheConstant bizName, String zSetName, String value, Double score) {
        try {
            redisTemplate.opsForZSet().add(bizName + zSetName, value, score);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}