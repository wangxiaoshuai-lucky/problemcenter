package com.kelab.problemcenter.dal.dao;

import com.kelab.problemcenter.dal.model.ProblemModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemMapper {

    /**
     * 通过 ids 查询题目信息
     */
    List<ProblemModel> queryByIds(@Param("ids") List<Integer> ids);

}
