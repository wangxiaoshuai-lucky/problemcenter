package com.kelab.problemcenter.dal.dao;

import com.kelab.info.problemcenter.query.ProblemNoteQuery;
import com.kelab.problemcenter.dal.model.ProblemNoteModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemNoteMapper {

    /**
     * 分页查询
     */
    List<ProblemNoteModel> queryPage(@Param("query") ProblemNoteQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(@Param("query") ProblemNoteQuery query);

    /**
     * 通过 ids 查询
     */
    List<ProblemNoteModel> queryByIds(@Param("ids")List<Integer> ids);

    /**
     * 添加标签
     */
    void save(@Param("record") ProblemNoteModel record);

    /**
     * 更新标签
     */
    void update(@Param("record") ProblemNoteModel record);

    /**
     * 删除标签
     */
    void delete(@Param("ids") List<Integer> ids);
}
