package com.kelab.problemcenter.dal.repo.impl;

import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.convert.ProblemConvert;
import com.kelab.problemcenter.dal.dao.ProblemMapper;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemSubmitInfoDomain;
import com.kelab.problemcenter.dal.model.ProblemModel;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.dal.repo.ProblemRepo;
import com.kelab.problemcenter.dal.repo.ProblemSubmitInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ProblemRepoImpl implements ProblemRepo {

    private ProblemMapper problemMapper;

    private ProblemSubmitInfoRepo problemSubmitInfoRepo;

    private RedisCache redisCache;

    @Autowired(required = false)
    public ProblemRepoImpl(ProblemMapper problemMapper,
                           ProblemSubmitInfoRepo problemSubmitInfoRepo,
                           RedisCache redisCache) {
        this.problemMapper = problemMapper;
        this.problemSubmitInfoRepo = problemSubmitInfoRepo;
        this.redisCache = redisCache;
    }

    @Override
    public List<ProblemDomain> queryByIds(List<Integer> ids, boolean withSubmitInfo) {
        List<ProblemModel> models = redisCache.cacheList(CacheBizName.PROBLEM_INFO, ids, ProblemModel.class, missKeyList -> {
            List<ProblemModel> dbModels = problemMapper.queryByIds(missKeyList);
            if (CollectionUtils.isEmpty(dbModels)) {
                return null;
            }
            return dbModels.stream().collect(Collectors.toMap(ProblemModel::getId, obj -> obj));
        });
        return convertAndFillSubmitInfo(models, withSubmitInfo);
    }


    private List<ProblemDomain> convertAndFillSubmitInfo(List<ProblemModel> models, boolean withSubmitInfo) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        List<ProblemDomain> result = new ArrayList<>(models.size());
        models.forEach(item -> {
            ProblemDomain single = ProblemConvert.modelToDomain(item);
            result.add(single);
        });
        // 填充提交信息
        if (withSubmitInfo) {
            List<Integer> probIds = result.stream().map(ProblemDomain::getId).collect(Collectors.toList());
            List<ProblemSubmitInfoDomain> submitInfoDomains = problemSubmitInfoRepo.queryByProbIds(probIds);
            Map<Integer, ProblemSubmitInfoDomain> submitMap = submitInfoDomains
                    .stream().collect(Collectors.toMap(ProblemSubmitInfoDomain::getProblemId, obj -> obj));
            result.forEach(item -> item.setSubmitInfoDomain(submitMap.get(item.getId())));
        }
        return result;
    }
}
