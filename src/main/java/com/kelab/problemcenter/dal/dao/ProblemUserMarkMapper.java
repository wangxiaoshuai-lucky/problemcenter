package com.kelab.problemcenter.dal.dao;

import com.kelab.problemcenter.constant.enums.MarkType;
import com.kelab.problemcenter.dal.model.ProblemUseMarkModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemUserMarkMapper {

    List<ProblemUseMarkModel> queryByUserIdAndTypes(@Param("userId") Integer userId, @Param("types") List<Integer> types);

    List<ProblemUseMarkModel> queryByUserIdAndProIdAndTypes(@Param("userId") Integer userId, @Param("probId") Integer probId, @Param("types") List<Integer> types);

    void save(@Param("userId") Integer userId, @Param("probId") Integer probId, @Param("type") Integer type);

    void delete(@Param("userId") Integer userId, @Param("probId") Integer probId, @Param("type") Integer type);
}
