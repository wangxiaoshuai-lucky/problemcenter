package com.kelab.problemcenter.dal.repo.impl;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.problemcenter.query.ProblemQuery;
import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.convert.ProblemConvert;
import com.kelab.problemcenter.dal.dao.ProblemMapper;
import com.kelab.problemcenter.dal.domain.ProblemAttachTagsDomain;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemSubmitInfoDomain;
import com.kelab.problemcenter.dal.domain.ProblemTagsDomain;
import com.kelab.problemcenter.dal.model.ProblemModel;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.dal.repo.ProblemAttachTagsRepo;
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

    private ProblemAttachTagsRepo problemAttachTagsRepo;

    private RedisCache redisCache;

    @Autowired(required = false)
    public ProblemRepoImpl(ProblemMapper problemMapper,
                           ProblemSubmitInfoRepo problemSubmitInfoRepo,
                           ProblemAttachTagsRepo problemAttachTagsRepo,
                           RedisCache redisCache) {
        this.problemMapper = problemMapper;
        this.problemSubmitInfoRepo = problemSubmitInfoRepo;
        this.problemAttachTagsRepo = problemAttachTagsRepo;
        this.redisCache = redisCache;
    }

    @Override
    public List<ProblemDomain> queryByIds(List<Integer> ids, boolean withSubmitInfo) {
        List<ProblemModel> models = redisCache.cacheList(CacheBizName.PROBLEM_INFO, ids, ProblemModel.class, missKeyList -> {
            List<ProblemModel> dbModels = problemMapper.queryByIds(missKeyList);
            if (CollectionUtils.isEmpty(dbModels)) {
                return null;
            }
            return dbModels.stream().collect(Collectors.toMap(ProblemModel::getId, obj -> obj, (v1, v2) -> v2));
        });
        return convertAndFillSubmitInfo(models, withSubmitInfo);
    }

    @Override
    @Verify(numberLimit = {"query.page [1, 100000]", "query.rows [1, 100000]"})
    public List<ProblemDomain> queryPage(ProblemQuery query) {
        return convertAndFillSubmitInfo(problemMapper.queryPage(query), true);
    }

    @Override
    public Integer queryTotal(ProblemQuery query) {
        return problemMapper.queryTotal(query);
    }

    @Override
    public void save(ProblemDomain record) {
        // 基本信息
        ProblemModel model = ProblemConvert.domainToModel(record);
        problemMapper.save(model);
        record.setId(model.getId());
        // 提交信息
        ProblemSubmitInfoDomain submitInfoDomain = record.getSubmitInfoDomain();
        submitInfoDomain.setProblemId(model.getId());
        problemSubmitInfoRepo.save(submitInfoDomain);
        // 标签信息
        List<ProblemTagsDomain> tagsDomains = record.getTagsDomains();
        if (!CollectionUtils.isEmpty(tagsDomains)) {
            List<ProblemAttachTagsDomain> attachDomains = new ArrayList<>(tagsDomains.size());
            tagsDomains.forEach(item -> {
                ProblemAttachTagsDomain domain = new ProblemAttachTagsDomain();
                domain.setProblemId(model.getId());
                domain.setTagsId(item.getId());
                attachDomains.add(domain);
            });
            problemAttachTagsRepo.saveList(attachDomains);
        }
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
                    .stream().collect(Collectors.toMap(ProblemSubmitInfoDomain::getProblemId, obj -> obj, (v1, v2) -> v2));
            result.forEach(item -> item.setSubmitInfoDomain(submitMap.get(item.getId())));
        }
        return result;
    }
}
