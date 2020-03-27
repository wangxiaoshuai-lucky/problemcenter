package com.kelab.problemcenter.dal.repo;

import com.kelab.problemcenter.dal.domain.LevelDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    void save(@Param("record") LevelDomain record);

    /**
     * 更新段位
     */
    void update(@Param("record") LevelDomain record);

    /**
     * 删除段位
     */
    void delete(@Param("ids") List<Integer> ids);
}
