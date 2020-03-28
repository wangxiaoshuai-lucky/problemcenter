package com.kelab.problemcenter.service;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.LevelInfo;
import com.kelab.problemcenter.dal.domain.LevelDomain;
import com.kelab.problemcenter.dal.domain.LevelProblemDomain;
import com.kelab.problemcenter.result.level.LevelProblemAdminResult;
import com.kelab.problemcenter.result.level.LevelProblemUserResult;

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

    /**
     * 通过段位id查询所有小段位的题目
     * 后台调用
     */
    LevelProblemAdminResult queryAllProblem(Context context, Integer levelId);


    /**
     * 通过段位id查询所有小段位的题目，
     * 过滤掉是否ac通过，训练调用
     */
    LevelProblemUserResult queryAllGradeInfo(Context context, Integer levelId);

    /**
     * 插入题目
     */
    void insertProblem(Context context, List<LevelProblemDomain> records);
}
