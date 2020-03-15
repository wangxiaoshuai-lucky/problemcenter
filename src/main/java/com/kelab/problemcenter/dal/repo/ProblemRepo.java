package com.kelab.problemcenter.dal.repo;

import com.kelab.problemcenter.dal.domain.ProblemDomain;

import java.util.List;

public interface ProblemRepo {

    /**
     * 通过 ids 查询题目，走缓存
     */
    List<ProblemDomain> queryByIds(List<Integer> ids, boolean withSubmitInfo);
}
