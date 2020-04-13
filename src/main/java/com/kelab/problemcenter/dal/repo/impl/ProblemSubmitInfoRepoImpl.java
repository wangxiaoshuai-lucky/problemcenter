package com.kelab.problemcenter.dal.repo.impl;

import com.kelab.problemcenter.convert.ProblemSubmitInfoConvert;
import com.kelab.problemcenter.dal.dao.ProblemSubmitInfoMapper;
import com.kelab.problemcenter.dal.domain.ProblemSubmitInfoDomain;
import com.kelab.problemcenter.dal.model.ProblemSubmitInfoModel;
import com.kelab.problemcenter.dal.repo.ProblemSubmitInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProblemSubmitInfoRepoImpl implements ProblemSubmitInfoRepo {


    private ProblemSubmitInfoMapper problemSubmitInfoMapper;

    @Autowired(required = false)
    public ProblemSubmitInfoRepoImpl(ProblemSubmitInfoMapper problemSubmitInfoMapper) {
        this.problemSubmitInfoMapper = problemSubmitInfoMapper;
    }

    @Override
    public List<ProblemSubmitInfoDomain> queryByProbIds(List<Integer> probIds) {
        List<ProblemSubmitInfoModel> models = problemSubmitInfoMapper.queryByProbIds(probIds);
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(ProblemSubmitInfoConvert::modelToDomain).collect(Collectors.toList());
    }

    @Override
    public void save(ProblemSubmitInfoDomain record) {
        ProblemSubmitInfoModel model = ProblemSubmitInfoConvert.domainToModel(record);
        problemSubmitInfoMapper.save(model);
        record.setId(model.getId());
    }

    @Override
    public void updateByProbId(Integer proId, boolean ac) {
        problemSubmitInfoMapper.updateByProbId(proId, ac);
    }
}
