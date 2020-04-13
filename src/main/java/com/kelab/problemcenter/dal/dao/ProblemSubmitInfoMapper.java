package com.kelab.problemcenter.dal.dao;

import com.kelab.problemcenter.dal.model.ProblemSubmitInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemSubmitInfoMapper {

    List<ProblemSubmitInfoModel> queryByProbIds(@Param("probIds") List<Integer> probIds);

    Integer save(@Param("record") ProblemSubmitInfoModel record);

    void updateByProbId(@Param("proId") Integer proId, @Param("ac") boolean ac);
}
