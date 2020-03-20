package com.kelab.problemcenter.service;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemSubmitRecordQuery;
import com.kelab.problemcenter.result.ProblemSubmitRecordInfo;

public interface ProblemSubmitRecordService {


    /**
     * 分页查询
     */
    PaginationResult<ProblemSubmitRecordInfo> queryPage(Context context, ProblemSubmitRecordQuery query);

    /**
     * 累计判题个数
     */
    Integer judgeCount(Context context);
}
