package com.kelab.problemcenter.dal.repo;

import com.kelab.problemcenter.dal.domain.ProblemSubmitInfoDomain;

import java.util.List;

public interface ProblemSubmitInfoRepo {

    List<ProblemSubmitInfoDomain> queryByProbIds(List<Integer> probIds);

    void save(ProblemSubmitInfoDomain record);
}
