package com.kelab.problemcenter.dal.dao;

import com.kelab.info.problemcenter.query.ProblemSubmitRecordQuery;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.problemcenter.dal.model.ProblemSubmitRecordModel;
import com.kelab.problemcenter.result.MilestoneResult;
import com.kelab.problemcenter.result.UserSubmitResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemSubmitRecordMapper {

    /**
     * 分页查询, 查询除了source和errorMsg之外的信息
     */
    List<ProblemSubmitRecordModel> queryPage(@Param("query") ProblemSubmitRecordQuery query);

    /**
     * 查询用户对应status的指定位置提交
     * 通过problemId去重
     */
    MilestoneResult queryUserStatus(@Param("userId") Integer userId, @Param("status") Integer status,
                                    @Param("num") Integer num);


    /**
     * 查询所有信息
     */
    List<ProblemSubmitRecordModel> queryByIds(@Param("ids") List<Integer> ids);

    /**
     * 查询条数
     */
    Integer queryTotal(@Param("query") ProblemSubmitRecordQuery query);

    /**
     * 保存提交记录
     */
    void saveSubmitRecord(@Param("record") ProblemSubmitRecordModel record);

    /**
     * 获取每个小时的submit数
     */
    List<OnlineStatisticResult> countSubmitDay(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 获取每个小时的ac数
     */
    List<OnlineStatisticResult> countAcDay(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 获取个人7天内的提交情况
     */
    List<UserSubmitResult> countWeek(@Param("userId") Integer userId,
                                     @Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
