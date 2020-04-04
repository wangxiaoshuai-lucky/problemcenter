package com.kelab.problemcenter.service;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.info.problemcenter.query.ProblemQuery;
import com.kelab.problemcenter.dal.domain.ProblemDomain;

import java.util.List;

public interface ProblemService {

    /**
     * 如果指定 ids：则准确查询题目信息
     * 否则条件查询已经审核通过的题目
     */
    PaginationResult<ProblemInfo> queryPage(Context context, ProblemQuery query);

    /**
     * 查询列表，指获取基本信息
     */
    List<ProblemInfo> queryByIds(Context context, List<Integer> ids);

    /**
     * 添加题目
     * 如果是管理员 直接审核通过
     * 如果是老师 待审核状态
     */
    void saveProblem(Context context, ProblemDomain record);

    /**
     * 删除题目
     */
    void deleteProblems(Context context, List<Integer> probIds);

    /**
     * 更新题目
     */
    void updateProblem(Context context, ProblemDomain record);

    /**
     * 题目总数
     */
    Integer problemTotal(Context context);

    /**
     * 查询 source
     */
    List<String> querySource(Context context, Integer limit);
}
