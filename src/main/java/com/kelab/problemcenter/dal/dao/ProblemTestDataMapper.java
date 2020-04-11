package com.kelab.problemcenter.dal.dao;

import com.kelab.problemcenter.dal.model.ProblemTestDataModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemTestDataMapper {

    List<ProblemTestDataModel> queryByProblemId(@Param("problemId") Integer problemId);

    ProblemTestDataModel queryById(@Param("id") Integer id);

    void delete(@Param("ids") List<Integer> ids);

    void saveList(@Param("records")  List<ProblemTestDataModel> records);

    void update(@Param("record") ProblemTestDataModel record);
}
