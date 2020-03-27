package com.kelab.problemcenter.dal.dao;

import com.kelab.problemcenter.dal.model.LevelModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LevelMapper {

    /**
     * 查询所有段位
     */
    List<LevelModel> queryAll();

    /**
     * 添加段位
     */
    void save(@Param("record") LevelModel record);

    /**
     * 更新段位
     */
    void update(@Param("record") LevelModel record);

    /**
     * 删除段位
     */
    void delete(@Param("ids") List<Integer> ids);
}
