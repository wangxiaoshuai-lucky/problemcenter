package com.kelab.problemcenter.dal.repo;

import com.kelab.problemcenter.dal.domain.LevelDomain;
import com.kelab.problemcenter.dal.domain.LevelProblemDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LevelRepo {

    /**
     * 查询所有段位,
     * 走缓存
     */
    List<LevelDomain> queryAll();

    /**
     * 添加段位
     */
    void save(LevelDomain record);

    /**
     * 更新段位
     */
    void update(LevelDomain record);

    /**
     * 删除段位
     */
    void delete(List<Integer> ids);


    /**
     * 查询段位关联的所有题目
     */
    List<LevelProblemDomain> queryLevelProblemByLevelId(Integer levelId);


    /**
     * 插入题目
     */
    void insertProblem(List<LevelProblemDomain> records);
}
