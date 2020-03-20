package com.kelab.problemcenter.dal.repo.impl;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.problemcenter.query.ProblemTagsQuery;
import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.convert.ProblemTagsConvert;
import com.kelab.problemcenter.dal.dao.ProblemTagsMapper;
import com.kelab.problemcenter.dal.domain.ProblemTagsDomain;
import com.kelab.problemcenter.dal.model.ProblemTagsModel;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.dal.repo.ProblemAttachTagsRepo;
import com.kelab.problemcenter.dal.repo.ProblemTagsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProblemTagsRepoImpl implements ProblemTagsRepo {

    private ProblemTagsMapper problemTagsMapper;

    private ProblemAttachTagsRepo problemAttachTagsRepo;

    private RedisCache redisCache;

    @Autowired(required = false)
    public ProblemTagsRepoImpl(ProblemTagsMapper problemTagsMapper,
                               ProblemAttachTagsRepo problemAttachTagsRepo,
                               RedisCache redisCache) {
        this.problemTagsMapper = problemTagsMapper;
        this.problemAttachTagsRepo = problemAttachTagsRepo;
        this.redisCache = redisCache;
    }


    @Override
    @Verify(numberLimit = {"query.page [1, 100000]", "query.rows [1, 100000]"})
    public List<ProblemTagsDomain> queryPage(ProblemTagsQuery query) {
        return convertToDomain(problemTagsMapper.queryPage(query));
    }

    @Override
    public Integer queryTotal(ProblemTagsQuery query) {
        return problemTagsMapper.queryTotal(query);
    }

    @Override
    public List<ProblemTagsDomain> queryByName(String name) {
        return convertToDomain(problemTagsMapper.queryByName(name));
    }

    @Override
    public List<ProblemTagsDomain> queryByIds(List<Integer> ids) {
        List<ProblemTagsModel> models = redisCache.cacheList(CacheBizName.PROBLEM_TAGS, ids, ProblemTagsModel.class, missKeyList -> {
            List<ProblemTagsModel> dbModels = problemTagsMapper.queryByIds(missKeyList);
            if (CollectionUtils.isEmpty(dbModels)) {
                return null;
            }
            return dbModels.stream().collect(Collectors.toMap(ProblemTagsModel::getId, obj -> obj, (v1, v2) -> v2));
        });
        return convertToDomain(models);
    }

    @Override
    public void save(ProblemTagsDomain record) {
        ProblemTagsModel model = ProblemTagsConvert.domainToModel(record);
        problemTagsMapper.save(model);
        record.setId(model.getId());
    }

    @Override
    public void update(ProblemTagsDomain record) {
        problemTagsMapper.update(ProblemTagsConvert.domainToModel(record));
        redisCache.delete(CacheBizName.PROBLEM_TAGS, record.getId());
    }

    @Override
    public void delete(List<Integer> ids) {
        problemTagsMapper.delete(ids);
        problemAttachTagsRepo.deleteByTagsIds(ids);
        redisCache.deleteList(CacheBizName.PROBLEM_TAGS, ids);
    }

    private List<ProblemTagsDomain> convertToDomain(List<ProblemTagsModel> problemTagsModels) {
        if (CollectionUtils.isEmpty(problemTagsModels)) {
            return Collections.emptyList();
        }
        return problemTagsModels.stream().map(ProblemTagsConvert::modelToDomain).collect(Collectors.toList());
    }
}
