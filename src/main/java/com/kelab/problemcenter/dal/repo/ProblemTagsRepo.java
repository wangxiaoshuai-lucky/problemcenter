package com.kelab.problemcenter.dal.repo;

import com.kelab.info.problemcenter.query.ProblemTagsQuery;
import com.kelab.problemcenter.dal.domain.ProblemTagsDomain;

import java.util.List;

public interface ProblemTagsRepo {
    /**
     * 分页查询
     */
    List<ProblemTagsDomain> queryPage(ProblemTagsQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(ProblemTagsQuery query);

    /**
     * 通过 name 查询
     */
    List<ProblemTagsDomain> queryByName(String name);

    /**
     * 通过 ids 查询, 缓存
     */
    List<ProblemTagsDomain> queryByIds(List<Integer> ids);

    /**
     * 添加标签
     */
    void save(ProblemTagsDomain record);

    /**
     * 添加标签
     */
    void update(ProblemTagsDomain record);

    /**
     * 删除标签
     */
    void delete(List<Integer> ids);
}
