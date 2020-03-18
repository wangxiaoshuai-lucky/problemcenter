package com.kelab.problemcenter.dal.repo.impl;

import com.kelab.problemcenter.convert.ProblemAttachTagsConvert;
import com.kelab.problemcenter.dal.dao.ProblemAttachTagsMapper;
import com.kelab.problemcenter.dal.domain.ProblemAttachTagsDomain;
import com.kelab.problemcenter.dal.model.ProblemAttachTagsModel;
import com.kelab.problemcenter.dal.repo.ProblemAttachTagsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ProblemAttachTagsRepoImpl implements ProblemAttachTagsRepo {

    private ProblemAttachTagsMapper problemAttachTagsMapper;

    @Autowired(required = false)
    public ProblemAttachTagsRepoImpl(ProblemAttachTagsMapper problemAttachTagsMapper) {
        this.problemAttachTagsMapper = problemAttachTagsMapper;
    }

    @Override
    public List<ProblemAttachTagsDomain> queryByTagsId(Integer tagsId) {
        return convertToDomain(problemAttachTagsMapper.queryByTagsId(tagsId));
    }

    @Override
    public List<ProblemAttachTagsDomain> queryByProblemIds(List<Integer> probIds) {
        return convertToDomain(problemAttachTagsMapper.queryByProblemIds(probIds));
    }

    @Override
    public List<ProblemAttachTagsDomain> queryByTagsName(String name) {
        return convertToDomain(problemAttachTagsMapper.queryByTagsName(name));
    }

    @Override
    public void saveList(List<ProblemAttachTagsDomain> records) {
        problemAttachTagsMapper.saveList(convertToModel(records));
    }

    @Override
    public void deleteByProblemIds(List<Integer> probIds) {
        problemAttachTagsMapper.deleteByProblemIds(probIds);
    }

    @Override
    public void deleteByTagsIds(List<Integer> tagsIds) {
        problemAttachTagsMapper.deleteByTagsIds(tagsIds);
    }

    private List<ProblemAttachTagsModel> convertToModel(List<ProblemAttachTagsDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        List<ProblemAttachTagsModel> models = new ArrayList<>();
        domains.forEach(item -> models.add(ProblemAttachTagsConvert.domainToModel(item)));
        return models;
    }

    private List<ProblemAttachTagsDomain> convertToDomain(List<ProblemAttachTagsModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        List<ProblemAttachTagsDomain> domains = new ArrayList<>();
        models.forEach(item -> domains.add(ProblemAttachTagsConvert.modelToDomain(item)));
        return domains;
    }
}
