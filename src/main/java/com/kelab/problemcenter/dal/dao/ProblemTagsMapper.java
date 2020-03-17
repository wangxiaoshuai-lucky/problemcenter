package com.kelab.problemcenter.dal.dao;

import com.kelab.info.problemcenter.query.ProblemTagsQuery;
import com.kelab.problemcenter.dal.model.ProblemTagsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemTagsMapper {

    /**
     * 分页查询
     */
    List<ProblemTagsModel> queryPage(@Param("query") ProblemTagsQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(@Param("query") ProblemTagsQuery query);

    /**
     * 通过 name 查询
     */
    List<ProblemTagsModel> queryByName(@Param("name") String name);

    /**
     * 通过 ids 查询
     */
    List<ProblemTagsModel> queryByIds(@Param("ids") List<Integer> ids);

}
