package com.kelab.problemcenter.dal.dao;

import com.kelab.problemcenter.dal.model.LevelProblemModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LevelProblemMapper {

    /**
     * 查询一个段位所有的题目
     */
    List<LevelProblemModel> queryByLevelId(@Param("levelId") Integer levelId);

    /**
     * 添加一个小段位的题目
     */
    void saveList(@Param("records") List<LevelProblemModel> records);

    /**
     * 删除所有段位下的所有题目
     */
    void deleteByLevelId(@Param("levelIds") List<Integer> levelIds);

    /**
     * 删除小段位下的所有题目
     */
    void deleteByLevelIdAndGrade(@Param("levelId") Integer levelId, @Param("grade") Integer grade);
}
