package com.kelab.problemcenter.service;

import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemTestDataInfo;
import com.kelab.problemcenter.dal.domain.ProblemTestDataDomain;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProblemTestDataService {

    List<ProblemTestDataInfo> queryByProblemId(Context context, Integer problemId);

    void updateProblemTestData(Context context, ProblemTestDataDomain record);

    void deleteProblemTestData(Context context, List<Integer> ids);

    /**
     * 下载题目下的所有判题数据
     */
    ResponseEntity<byte[]> downloadTestData(Context context, Integer problemId, String key);
}
