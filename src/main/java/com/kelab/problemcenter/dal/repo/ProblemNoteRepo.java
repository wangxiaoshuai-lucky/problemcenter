package com.kelab.problemcenter.dal.repo;

import com.kelab.info.problemcenter.query.ProblemNoteQuery;
import com.kelab.problemcenter.dal.domain.ProblemNoteDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemNoteRepo {

    /**
     * 分页查询
     */
    List<ProblemNoteDomain> queryPage(ProblemNoteQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(ProblemNoteQuery query);

    /**
     * 通过 userId、 problemId 查询
     */
    ProblemNoteDomain queryByUserIdAndProbId(Integer userId, Integer probId);


    /**
     * 通过 ids 查询
     */
    List<ProblemNoteDomain> queryByIds(List<Integer> ids);

    /**
     * 添加标签
     */
    void save(ProblemNoteDomain record);

    /**
     * 更新标签
     */
    void update(ProblemNoteDomain record);

    /**
     * 删除标签
     */
    void delete(List<Integer> ids);
}
