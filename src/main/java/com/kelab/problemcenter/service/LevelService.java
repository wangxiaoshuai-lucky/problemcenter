package com.kelab.problemcenter.service;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.LevelInfo;
import com.kelab.problemcenter.dal.domain.LevelDomain;

import java.util.List;

public interface LevelService {

    /**
     * 查询所有段位
     */
    PaginationResult<LevelInfo> queryAllLevel(Context context);

    /**
     * 添加段位
     */
    void saveLevel(Context context, LevelDomain record);

    /**
     * 更新段位
     */
    void updateLevel(Context context, LevelDomain record);

    /**
     * 删除段位
     */
    void deleteLevel(Context context, List<Integer> ids);
}
