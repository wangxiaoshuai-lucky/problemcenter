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

import java.util.ArrayList;
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

    private List<Integer> buildTypes(List<MarkType> types) {
        List<Integer> markTypes = new ArrayList<>(types.size());
        types.forEach(item -> markTypes.add(item.value()));
        return markTypes;
    }

    private List<ProblemUserMarkDomain> convertAndFillTitle(List<ProblemUseMarkModel> models) {
        List<ProblemUserMarkDomain> result = new ArrayList<>(models.size());
        models.forEach(item -> result.add(ProblemUserMarkConvert.modelToDomain(item)));
        // 填充title
        List<Integer> problemIds = result.stream().map(ProblemUserMarkDomain::getProblemId).collect(Collectors.toList());
        List<ProblemDomain> problemDomains = problemRepo.queryByIds(problemIds, false);
        Map<Integer, String> idTitleMap = problemDomains.stream().collect(Collectors.toMap(ProblemDomain::getId, ProblemDomain::getTitle, (v1, v2) -> v2));
        result.forEach(item -> item.setTitle(idTitleMap.get(item.getProblemId())));
        return result.stream().filter(item -> StringUtils.isNotBlank(item.getTitle())).collect(Collectors.toList());
    }
}
