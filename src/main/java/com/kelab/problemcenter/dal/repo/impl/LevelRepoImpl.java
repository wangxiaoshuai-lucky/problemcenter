package com.kelab.problemcenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.convert.LevelConvert;
import com.kelab.problemcenter.dal.dao.LevelMapper;
import com.kelab.problemcenter.dal.domain.LevelDomain;
import com.kelab.problemcenter.dal.model.LevelModel;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.dal.repo.LevelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LevelRepoImpl implements LevelRepo {


    private LevelMapper levelMapper;

    private RedisCache redisCache;

    @Autowired(required = false)
    public LevelRepoImpl(LevelMapper levelMapper,
                         RedisCache redisCache) {
        this.levelMapper = levelMapper;
        this.redisCache = redisCache;
    }

    @Override
    public List<LevelDomain> queryAll() {
        String cacheObj = redisCache.cacheOne(CacheBizName.LEVEL, "ALL",
                String.class, missKey -> JSON.toJSONString(levelMapper.queryAll()));
        return convertToDomain(JSON.parseArray(cacheObj, LevelModel.class));
    }

    @Override
    public void save(LevelDomain record) {
        levelMapper.save(LevelConvert.domainToModel(record));
        // 删除段位缓存
        redisCache.delete(CacheBizName.LEVEL, "ALL");
    }

    @Override
    public void update(LevelDomain record) {
        levelMapper.update(LevelConvert.domainToModel(record));
        // 删除段位缓存
        redisCache.delete(CacheBizName.LEVEL, "ALL");
    }

    @Override
    public void delete(List<Integer> ids) {
        levelMapper.delete(ids);
        // 删除段位缓存
        redisCache.delete(CacheBizName.LEVEL, "ALL");
    }

    private List<LevelDomain> convertToDomain(List<LevelModel> levelModels) {
        if (CollectionUtils.isEmpty(levelModels)) {
            return Collections.emptyList();
        }
        return levelModels.stream().map(LevelConvert::modelToDomain).collect(Collectors.toList());
    }
}
