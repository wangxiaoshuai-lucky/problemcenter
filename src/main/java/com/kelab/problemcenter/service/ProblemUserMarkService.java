package com.kelab.problemcenter.service;

import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemUserMarkInfo;
import com.kelab.problemcenter.constant.enums.MarkType;

import java.util.List;
import java.util.Map;

public interface ProblemUserMarkService {

    Map<MarkType, List<ProblemUserMarkInfo>> queryAcAndChallenging(Context context, Integer userId);

    List<ProblemUserMarkInfo> collect(Context context);

    void deleteOrSave(Context context, ProblemUserMarkInfo record);

    /**
     * 查询用户在截止时间之前指定题目的ac记录
     */
    List<ProblemUserMarkInfo> queryByUserIdsAndProbIdsAndEndTime(Context context, List<Integer> userIds, List<Integer> probIds, Long endTime);
}
