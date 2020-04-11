package com.kelab.problemcenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.convert.ProblemTestDataConvert;
import com.kelab.problemcenter.dal.dao.ProblemTestDataMapper;
import com.kelab.problemcenter.dal.domain.ProblemTestDataDomain;
import com.kelab.problemcenter.dal.model.ProblemTestDataModel;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.dal.repo.ProblemTestDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProblemTestDataRepoImpl implements ProblemTestDataRepo {

    private ProblemTestDataMapper problemTestDataMapper;

    private RedisCache redisCache;

    @Autowired(required = false)
    public ProblemTestDataRepoImpl(ProblemTestDataMapper problemTestDataMapper,
                                   RedisCache redisCache) {
        this.problemTestDataMapper = problemTestDataMapper;
        this.redisCache = redisCache;
    }

    @Override
    public List<ProblemTestDataDomain> queryByProblemId(Integer problemId) {
        String cacheObj = redisCache.cacheOne(CacheBizName.PROBLEM_TEST_DATA, problemId,
                String.class, missKey -> JSON.toJSONString(problemTestDataMapper.queryByProblemId(missKey)));
        return convertToDomain(JSON.parseArray(cacheObj, ProblemTestDataModel.class));
    }

    @Override
    public void delete(List<Integer> ids) {
        ProblemTestDataModel old = problemTestDataMapper.queryById(ids.get(0));
        if (old != null) {
            problemTestDataMapper.delete(ids);
            redisCache.delete(CacheBizName.PROBLEM_TEST_DATA, old.getProblemId());
        }
    }

    @Override
    public void saveList(List<ProblemTestDataDomain> records) {
        problemTestDataMapper.saveList(records.stream().map(ProblemTestDataConvert::domainToModel).collect(Collectors.toList()));
        redisCache.delete(CacheBizName.PROBLEM_TEST_DATA, records.get(0).getProblemId());
    }

    @Override
    public void update(ProblemTestDataDomain record) {
        ProblemTestDataModel old = problemTestDataMapper.queryById(record.getId());
        if (old != null) {
            problemTestDataMapper.update(ProblemTestDataConvert.domainToModel(record));
            redisCache.delete(CacheBizName.PROBLEM_TEST_DATA, old.getProblemId());
        }
    }

    private List<ProblemTestDataDomain> convertToDomain(List<ProblemTestDataModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(ProblemTestDataConvert::modelToDomain).collect(Collectors.toList());
    }
}
