package com.kelab.problemcenter.dal.repo;

import com.kelab.info.problemcenter.query.ProblemQuery;
import com.kelab.problemcenter.dal.domain.ProblemDomain;

import java.util.List;

public interface ProblemRepo {

    /**
     * 通过 ids 查询题目，走缓存
     */
    List<ProblemDomain> queryByIds(List<Integer> ids, boolean withSubmitInfo);

    /**
     * 分页查询, 如果前端带有 ids, 则尽量调用 queryByIds 方法，可以走缓存
     */
    List<ProblemDomain> queryPage(ProblemQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(ProblemQuery query);

    /**
     * 保存题目
     */
    void save(ProblemDomain record);
}
