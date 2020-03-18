package com.kelab.problemcenter.service;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemTagsInfo;
import com.kelab.info.problemcenter.query.ProblemTagsQuery;
import com.kelab.problemcenter.dal.domain.ProblemTagsDomain;

import java.util.List;

public interface ProblemTagsService {


    /**
     * 如果指定 ids：则准确查询
     * 否则条件查询
     */
    PaginationResult<ProblemTagsInfo> queryPage(Context context, ProblemTagsQuery query);


    /**
     * 添加标签
     */
    void save(Context context, ProblemTagsDomain record);

    /**
     * 更新标签
     */
    void update(Context context, ProblemTagsDomain record);

    /**
     * 删除标签
     */
    void delete(Context context, List<Integer> ids);
}
