package com.kelab.problemcenter.dal.repo;

import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.query.ProblemSubmitRecordQuery;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.problemcenter.constant.enums.ProblemJudgeStatus;
import com.kelab.problemcenter.dal.domain.ProblemSubmitRecordDomain;
import com.kelab.problemcenter.dal.domain.SubmitRecordFilterDomain;
import com.kelab.problemcenter.dal.model.ProblemSubmitRecordModel;
import com.kelab.problemcenter.result.MilestoneResult;
import com.kelab.problemcenter.result.UserSubmitResult;

import java.util.List;
import java.util.Map;

public interface ProblemSubmitRecordRepo {

    /**
     * 分页查询
     */
    List<ProblemSubmitRecordDomain> queryPage(Context context, ProblemSubmitRecordQuery query, SubmitRecordFilterDomain filter);

    /**
     * 查询所有信息,带缓存
     */
    List<ProblemSubmitRecordDomain> queryByIds(Context context, List<Integer> ids, SubmitRecordFilterDomain filter);

    /**
     * 查询条数
     */
    Integer queryTotal(ProblemSubmitRecordQuery query);

    /**
     * 保存提交记录
     */
    void saveSubmitRecord(ProblemSubmitRecordDomain record);

    /**
     * 获取每个小时的ac和submit
     * 走缓存，endTime当前的整点时间, startTime昨天的整点时间
     */
    Map<String, OnlineStatisticResult> countDay(Long startTime, Long endTime);


    /**
     * 查询用户对应status的指定位置提交记录
     * 通过problemId去重
     */
    MilestoneResult queryUserStatus(Integer userId, ProblemJudgeStatus status, Integer num);

    /**
     * 获取一段时间的提交情况
     */
    List<UserSubmitResult> queryCountWeek(Integer userId,Long startTime, Long endTime);

}
