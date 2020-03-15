package com.kelab.problemcenter.dal.repo.impl;

import com.kelab.problemcenter.convert.ProblemConvert;
import com.kelab.problemcenter.convert.ProblemSubmitInfoConvert;
import com.kelab.problemcenter.dal.dao.ProblemSubmitInfoMapper;
import com.kelab.problemcenter.dal.domain.ProblemSubmitInfoDomain;
import com.kelab.problemcenter.dal.model.ProblemSubmitInfoModel;
import com.kelab.problemcenter.dal.repo.ProblemSubmitInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<ProblemSubmitInfoDomain> result = new ArrayList<>(models.size());
        models.forEach(item-> result.add(ProblemSubmitInfoConvert.modelToDomain(item)));
        return result;
    }
}
