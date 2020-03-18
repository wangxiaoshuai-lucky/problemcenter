package com.kelab.problemcenter.dal.repo;

import com.kelab.problemcenter.dal.domain.ProblemAttachTagsDomain;

import java.util.List;

public interface ProblemAttachTagsRepo {

    /**
     * 通过 probIds 找关联记录
     */
    List<ProblemAttachTagsDomain> queryByProblemIds(List<Integer> probIds);

    /**
     * 通过 tagsId 找关联记录
     */
    List<ProblemAttachTagsDomain> queryByTagsId(Integer tagsId);

    /**
     * 通过 tagName 找关联记录
     */
    List<ProblemAttachTagsDomain> queryByTagsName(String name);

    /**
     * 插入列表
     */
    void saveList(List<ProblemAttachTagsDomain> records);

    /**
     * 删除题目-标签关联
     */
    void deleteByProblemIds(List<Integer> probIds);

    /**
     * 删除题目-标签关联
     */
    void deleteByTagsIds(List<Integer> tagsIds);
}
