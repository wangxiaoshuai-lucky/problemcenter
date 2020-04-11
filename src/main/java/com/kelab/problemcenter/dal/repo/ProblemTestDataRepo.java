package com.kelab.problemcenter.dal.repo;

import com.kelab.problemcenter.dal.domain.ProblemTestDataDomain;

import java.util.List;

public interface ProblemTestDataRepo {

    List<ProblemTestDataDomain> queryByProblemId(Integer problemId);

    void delete(List<Integer> ids);

    void saveList(List<ProblemTestDataDomain> records);

    void update(ProblemTestDataDomain record);
}
