package com.kelab.problemcenter.dal.dao;

import com.kelab.info.problemcenter.query.ProblemQuery;
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


    /**
     * 通过 title, source, orderByTitle, orderByDifficult 查询, 以及 source关联到的 ids 查询
     */
    List<ProblemModel> queryPage(@Param("query") ProblemQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(@Param("query") ProblemQuery query);

    /**
     * 保存题目基本信息
     */
    Integer save(@Param("record") ProblemModel model);

    /**
     * 删除题目
     */
    void delete(@Param("ids") List<Integer> ids);

    /**
     * 保存题目基本信息
     */
    void update(@Param("record") ProblemModel model);


    /**
     * 查询 source 列表
     */
    List<String> querySource(@Param("limit") Integer limit);

}
