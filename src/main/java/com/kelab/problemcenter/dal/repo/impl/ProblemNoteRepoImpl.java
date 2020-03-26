package com.kelab.problemcenter.dal.repo.impl;

import com.kelab.info.problemcenter.query.ProblemNoteQuery;
import com.kelab.problemcenter.convert.ProblemNoteConvert;
import com.kelab.problemcenter.dal.dao.ProblemNoteMapper;
import com.kelab.problemcenter.dal.domain.ProblemNoteDomain;
import com.kelab.problemcenter.dal.model.ProblemNoteModel;
import com.kelab.problemcenter.dal.repo.ProblemNoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProblemNoteRepoImpl implements ProblemNoteRepo {

    private ProblemNoteMapper problemNoteMapper;

    @Autowired(required = false)
    public ProblemNoteRepoImpl(ProblemNoteMapper problemNoteMapper) {
        this.problemNoteMapper = problemNoteMapper;
    }


    @Override
    public List<ProblemNoteDomain> queryPage(ProblemNoteQuery query) {
        return convertToDomain(problemNoteMapper.queryPage(query));
    }

    @Override
    public Integer queryTotal(ProblemNoteQuery query) {
        return problemNoteMapper.queryTotal(query);
    }

    @Override
    public ProblemNoteDomain queryByUserIdAndProbId(Integer userId, Integer probId) {
        return ProblemNoteConvert.modelToDomain(problemNoteMapper.queryByUserIdAndProbId(userId, probId));
    }

    @Override
    public List<ProblemNoteDomain> queryByIds(List<Integer> ids) {
        return convertToDomain(problemNoteMapper.queryByIds(ids));
    }

    @Override
    public void save(ProblemNoteDomain record) {
        problemNoteMapper.save(ProblemNoteConvert.domainToModel(record));
    }

    @Override
    public void update(ProblemNoteDomain record) {
        problemNoteMapper.update(ProblemNoteConvert.domainToModel(record));
    }

    @Override
    public void delete(List<Integer> ids) {
        problemNoteMapper.delete(ids);
    }

    private List<ProblemNoteDomain> convertToDomain(List<ProblemNoteModel> problemTagsModels) {
        if (CollectionUtils.isEmpty(problemTagsModels)) {
            return Collections.emptyList();
        }
        return problemTagsModels.stream().map(ProblemNoteConvert::modelToDomain).collect(Collectors.toList());
    }
}
