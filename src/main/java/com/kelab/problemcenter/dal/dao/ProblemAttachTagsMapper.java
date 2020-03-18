package com.kelab.problemcenter.dal.dao;

import com.kelab.problemcenter.dal.model.ProblemAttachTagsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemAttachTagsMapper {

    /**
     * 通过 probId 找关联记录
     */
    List<ProblemAttachTagsModel> queryByProblemIds(@Param("probIds") List<Integer> probId);

    /**
     * 通过 tagsId 找关联记录
     */
    List<ProblemAttachTagsModel> queryByTagsId(@Param("tagsId") Integer tagsId);

    /**
     * 通过 tagName 找关联记录
     */
    List<ProblemAttachTagsModel> queryByTagsName(@Param("name") String name);

    /**
     * 插入列表
     */
    void saveList(@Param("records") List<ProblemAttachTagsModel> records);

    /**
     * 删除题目-标签关联
     */
    void deleteByProblemIds(@Param("probIds") List<Integer> probIds);

    /**
     * 删除题目-标签关联
     */
    void deleteByTagsIds(@Param("tagsIds") List<Integer> tagsIds);
}
