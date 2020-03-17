package com.kelab.problemcenter.service;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.info.problemcenter.info.ProblemTagsInfo;
import com.kelab.info.problemcenter.query.ProblemQuery;
import com.kelab.info.problemcenter.query.ProblemTagsQuery;
import com.kelab.problemcenter.dal.domain.ProblemDomain;

public interface ProblemService {

    /**
     * 如果指定 ids：则准确查询题目信息
     * 否则条件查询已经审核通过的题目
     */
    PaginationResult<ProblemInfo> queryPage(Context context, ProblemQuery query);

    /**
     * 添加题目
     * 如果是管理员 直接审核通过
     * 如果是老师 待审核状态
     */
    void saveProblem(Context context, ProblemDomain record);

    /**
     * 如果指定 ids：则准确查询
     * 否则条件查询
     */
    PaginationResult<ProblemTagsInfo> queryPage(Context context, ProblemTagsQuery query);
}
