package com.kelab.problemcenter.dal.dao;

import com.kelab.problemcenter.dal.model.ProblemUseMarkModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemUserMarkMapper {

    List<ProblemUseMarkModel> queryByUserIdAndTypes(@Param("userId") Integer userId, @Param("types") List<Integer> types);

    List<ProblemUseMarkModel> queryByUserIdAndProIdsAndTypes(@Param("userId") Integer userId, @Param("probIds") List<Integer> probIds, @Param("types") List<Integer> types);

    void save(@Param("record") ProblemUseMarkModel record);

    /**
     * 只更新关联id
     */
    void update(@Param("record") ProblemUseMarkModel record);

    void delete(@Param("userId") Integer userId, @Param("probId") Integer probId, @Param("type") Integer type);

    List<ProblemUseMarkModel> queryByUserIdsAndProbIdsAndEndTime(@Param("userIds") List<Integer> userIds,
                                                                 @Param("probIds") List<Integer> probIds,
                                                                 @Param("types") List<Integer> types,
                                                                 @Param("endTime") Long endTime);
}
