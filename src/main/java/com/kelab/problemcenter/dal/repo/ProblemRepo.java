package com.kelab.problemcenter.dal.repo;

import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.query.ProblemQuery;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemFilterDomain;

import java.util.List;

public interface ProblemRepo {

    /**
     * 通过 ids 查询题目，走缓存
     */
    List<ProblemDomain> queryByIds(Context context, List<Integer> ids, ProblemFilterDomain filter);

    /**
     * 分页查询, 如果前端带有 ids, 则尽量调用 queryByIds 方法，可以走缓存
     */
    List<ProblemDomain> queryPage(Context context, ProblemQuery query, ProblemFilterDomain filter);

    /**
     * 查询条数
     */
    Integer queryTotal(ProblemQuery query);

    /**
     * 保存题目
     */
    void save(ProblemDomain record);

    /**
     * 删除题目
     */
    void delete(List<Integer> ids);

    /**
     * 更新题目
     */
    void update(ProblemDomain record);

    /**
     * 查询 source, 万年不变，走缓存
     */
    List<String> querySource(Integer limit);
}
