package com.kelab.problemcenter.dal.repo.impl;

import com.kelab.problemcenter.constant.enums.MarkType;
import com.kelab.problemcenter.convert.ProblemUserMarkConvert;
import com.kelab.problemcenter.dal.dao.ProblemUserMarkMapper;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemUserMarkDomain;
import com.kelab.problemcenter.dal.model.ProblemUseMarkModel;
import com.kelab.problemcenter.dal.repo.ProblemRepo;
import com.kelab.problemcenter.dal.repo.ProblemUserMarkRepo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ProblemUserMarkRepoImpl implements ProblemUserMarkRepo {

    private ProblemUserMarkMapper problemUserMarkMapper;

    private ProblemRepo problemRepo;

    @Autowired(required = false)
    public ProblemUserMarkRepoImpl(ProblemUserMarkMapper problemUserMarkMapper,
                                   ProblemRepo problemRepo) {
        this.problemUserMarkMapper = problemUserMarkMapper;
        this.problemRepo = problemRepo;
    }

    @Override
    public List<ProblemUserMarkDomain> queryByUserIdAndTypes(Integer userId, List<MarkType> types) {
        List<Integer> markTypes = buildTypes(types);
        List<ProblemUseMarkModel> models = problemUserMarkMapper.queryByUserIdAndTypes(userId, markTypes);
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return convertAndFillTitle(models);
    }

    @Override
    public List<ProblemUserMarkDomain> queryByUserIdAndProIdsAndTypes(Integer userId, List<Integer> problemIds, List<MarkType> types) {
        List<Integer> markTypes = buildTypes(types);
        List<ProblemUseMarkModel> models = problemUserMarkMapper.queryByUserIdAndProIdsAndTypes(userId, problemIds, markTypes);
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return convertAndFillTitle(models);
    }

    @Override
    public void save(Integer userId, Integer problemId, MarkType markType) {
        problemUserMarkMapper.save(userId, problemId, markType.value());
    }

    @Override
    public void delete(Integer userId, Integer problemId, MarkType markType) {
        problemUserMarkMapper.delete(userId, problemId, markType.value());
    }

    @Override
    public List<ProblemUserMarkDomain> queryByUserIdsAndProbIdsAndEndTime(List<Integer> userIds, List<Integer> probIds, Long endTime) {
        return convertAndFillTitle(problemUserMarkMapper.queryByUserIdsAndProbIdsAndEndTime(userIds, probIds, endTime));
    }

    private List<Integer> buildTypes(List<MarkType> types) {
        return types.stream().map(MarkType::value).collect(Collectors.toList());
    }

    private List<ProblemUserMarkDomain> convertAndFillTitle(List<ProblemUseMarkModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        List<ProblemUserMarkDomain> result = models.stream().map(ProblemUserMarkConvert::modelToDomain).collect(Collectors.toList());
        // 填充title
        List<Integer> problemIds = result.stream().map(ProblemUserMarkDomain::getProblemId).collect(Collectors.toList());
        Map<Integer, ProblemDomain> problemMap = problemRepo.queryProblemMapByIds(null, problemIds, null);
        result.forEach(item -> item.setTitle(problemMap.getOrDefault(item.getProblemId(), new ProblemDomain()).getTitle()));
        return result.stream().filter(item -> StringUtils.isNotBlank(item.getTitle())).collect(Collectors.toList());
    }
}
