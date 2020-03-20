package com.kelab.problemcenter.dal.repo.impl;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemSubmitRecordQuery;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.convert.ProblemSubmitRecordConvert;
import com.kelab.problemcenter.dal.dao.ProblemSubmitRecordMapper;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemSubmitRecordDomain;
import com.kelab.problemcenter.dal.domain.SubmitRecordFilterDomain;
import com.kelab.problemcenter.dal.model.ProblemSubmitRecordModel;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.dal.repo.ProblemRepo;
import com.kelab.problemcenter.dal.repo.ProblemSubmitRecordRepo;
import com.kelab.problemcenter.support.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ProblemSubmitRecordRepoImpl implements ProblemSubmitRecordRepo {


    private ProblemSubmitRecordMapper problemSubmitRecordMapper;

    private ProblemRepo problemRepo;

    private UserCenterService userCenterService;

    private RedisCache redisCache;

    @Autowired(required = false)
    public ProblemSubmitRecordRepoImpl(ProblemSubmitRecordMapper problemSubmitRecordMapper,
                                       ProblemRepo problemRepo,
                                       UserCenterService userCenterService,
                                       RedisCache redisCache) {
        this.problemSubmitRecordMapper = problemSubmitRecordMapper;
        this.problemRepo = problemRepo;
        this.userCenterService = userCenterService;
        this.redisCache = redisCache;
    }

    @Override
    public Integer queryTotal(ProblemSubmitRecordQuery query) {
        return problemSubmitRecordMapper.queryTotal(query);
    }

    @Override
    @Verify(numberLimit = {"query.page [1, 100000]", "query.rows [1, 100000]"})
    public List<ProblemSubmitRecordDomain> queryPage(Context context, ProblemSubmitRecordQuery query, SubmitRecordFilterDomain filter) {
        return convertAndFillProblemAndUserInfo(context, problemSubmitRecordMapper.queryPage(query), filter);
    }

    @Override
    public List<ProblemSubmitRecordDomain> queryByIds(Context context, List<Integer> ids, SubmitRecordFilterDomain filter) {
        List<ProblemSubmitRecordModel> models = redisCache.cacheList(CacheBizName.PROBLEM_SUBMIT_RECORD, ids,
                ProblemSubmitRecordModel.class, missKeyList -> {
                    List<ProblemSubmitRecordModel> dbModels = problemSubmitRecordMapper.queryByIds(ids);
                    if (CollectionUtils.isEmpty(dbModels)) {
                        return null;
                    }
                    return dbModels.stream().collect(Collectors.toMap(ProblemSubmitRecordModel::getId, obj -> obj, (v1, v2) -> v2));
                });
        return convertAndFillProblemAndUserInfo(context, models, filter);
    }

    /**
     * 填充题目信息和提交者信息
     */
    private List<ProblemSubmitRecordDomain> convertAndFillProblemAndUserInfo(Context context, List<ProblemSubmitRecordModel> models, SubmitRecordFilterDomain filter) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        List<ProblemSubmitRecordDomain> result = models.stream().map(ProblemSubmitRecordConvert::modelToDomain).collect(Collectors.toList());
        // 获取题目信息
        if (filter != null && filter.isWithProblemInfo()) {
            List<Integer> probIds = result.stream().map(ProblemSubmitRecordDomain::getProblemId).collect(Collectors.toList());
            Map<Integer, ProblemDomain> probMap = problemRepo.queryByIds(context, probIds, null).stream().collect(Collectors.toMap(ProblemDomain::getId, obj -> obj, (v1, v2) -> v2));
            result.forEach(item -> item.setProblemInfo(probMap.get(item.getProblemId())));
        }
        // 获取用户信息
        if (filter != null && filter.isWithCreatorInfo()) {
            List<Integer> userIds = result.stream().map(ProblemSubmitRecordDomain::getUserId).collect(Collectors.toList());
            Map<Integer, UserInfo> userMap = userCenterService.queryByUserIds(context, userIds).stream().collect(Collectors.toMap(UserInfo::getId, obj -> obj, (v1, v2) -> v2));
            result.forEach(item -> item.setUserInfo(userMap.get(item.getUserId())));
        }
        return result;
    }
}
