package com.kelab.problemcenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.convert.LevelConvert;
import com.kelab.problemcenter.dal.dao.LevelMapper;
import com.kelab.problemcenter.dal.dao.LevelProblemMapper;
import com.kelab.problemcenter.dal.domain.LevelDomain;
import com.kelab.problemcenter.dal.domain.LevelProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.model.LevelModel;
import com.kelab.problemcenter.dal.model.LevelProblemModel;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.dal.repo.LevelRepo;
import com.kelab.problemcenter.dal.repo.ProblemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LevelRepoImpl implements LevelRepo {


    private LevelMapper levelMapper;

    private RedisCache redisCache;

    private LevelProblemMapper levelProblemMapper;

    private ProblemRepo problemRepo;

    @Autowired(required = false)
    public LevelRepoImpl(LevelMapper levelMapper,
                         RedisCache redisCache,
                         LevelProblemMapper levelProblemMapper,
                         ProblemRepo problemRepo) {
        this.levelMapper = levelMapper;
        this.redisCache = redisCache;
        this.levelProblemMapper = levelProblemMapper;
        this.problemRepo = problemRepo;
    }

    @Override
    public List<LevelDomain> queryAll() {
        String cacheObj = redisCache.cacheOne(CacheBizName.LEVEL, "ALL",
                String.class, missKey -> JSON.toJSONString(levelMapper.queryAll()));
        return convertToLevelDomain(JSON.parseArray(cacheObj, LevelModel.class));
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
        levelProblemMapper.deleteByLevelId(ids);
        // 删除段位缓存
        redisCache.delete(CacheBizName.LEVEL, "ALL");
    }

    @Override
    public List<LevelProblemDomain> queryLevelProblemByLevelId(Integer levelId) {
        String cacheObj = redisCache.cacheOne(CacheBizName.LEVEL_PROBLEM, levelId,
                String.class, missKey -> JSON.toJSONString(levelProblemMapper.queryByLevelId(levelId)));
        return convertToLevelProblemDomain(JSON.parseArray(cacheObj, LevelProblemModel.class));
    }

    @Override
    public List<LevelProblemDomain> queryAllBelowTheLevel(Integer levelId) {
        String cacheObj = redisCache.cacheOne(CacheBizName.LEVEL_PROBLEM, "below::" + levelId,
                String.class, missKey -> JSON.toJSONString(levelProblemMapper.queryAllBelowTheLevel(levelId)));
        return convertToLevelProblemDomain(JSON.parseArray(cacheObj, LevelProblemModel.class));
    }

    @Override
    public void insertProblem(List<LevelProblemDomain> records) {
        List<LevelProblemModel> collect = records.stream().map(LevelConvert::domainToModel).collect(Collectors.toList());
        levelProblemMapper.deleteByLevelIdAndGrade(collect.get(0).getLevelId(), collect.get(0).getGrade());
        levelProblemMapper.saveList(collect);
        // 删除这个段位的题目缓存
        redisCache.delete(CacheBizName.LEVEL_PROBLEM, collect.get(0).getLevelId());
        // 删除段位之前的题目缓存
        redisCache.delete(CacheBizName.LEVEL_PROBLEM, "below::" + collect.get(0).getLevelId());
    }

    private List<LevelDomain> convertToLevelDomain(List<LevelModel> levelModels) {
        if (CollectionUtils.isEmpty(levelModels)) {
            return Collections.emptyList();
        }
        return levelModels.stream().map(LevelConvert::modelToDomain).collect(Collectors.toList());
    }

    private List<LevelProblemDomain> convertToLevelProblemDomain(List<LevelProblemModel> levelModels) {
        if (CollectionUtils.isEmpty(levelModels)) {
            return Collections.emptyList();
        }
        List<LevelProblemDomain> domains = levelModels.stream().map(LevelConvert::modelToDomain).collect(Collectors.toList());
        // 填充title
        Map<Integer, ProblemDomain> problemMap = problemRepo.queryProblemMapByIds(null,
                domains.stream().map(LevelProblemDomain::getProId).collect(Collectors.toList()), null);
        domains.forEach(item -> item.setTitle(problemMap.getOrDefault(item.getProId(), new ProblemDomain()).getTitle()));
        return domains;
    }
}
