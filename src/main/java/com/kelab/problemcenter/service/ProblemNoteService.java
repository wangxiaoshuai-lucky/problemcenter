package com.kelab.problemcenter.service;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemNoteInfo;
import com.kelab.info.problemcenter.query.ProblemNoteQuery;
import com.kelab.problemcenter.dal.domain.ProblemNoteDomain;

import java.util.List;

public interface ProblemNoteService {

    /**
     * 分页查询
     */
    PaginationResult<ProblemNoteInfo> queryPage(Context context, ProblemNoteQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(Context context, ProblemNoteQuery query);

    /**
     * 添加笔记
     */
    void save(Context context, ProblemNoteDomain record);

    /**
     * 更新笔记
     */
    void update(Context context, ProblemNoteDomain record);

    /**
     * 删除笔记
     */
    void delete(Context context, List<Integer> ids);
}
