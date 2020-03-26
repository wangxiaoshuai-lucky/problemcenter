package com.kelab.problemcenter.dal.repo.impl;

import cn.wzy.verifyUtils.annotation.Verify;
import com.alibaba.fastjson.JSON;
import com.kelab.info.base.constant.UserRoleConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.query.ProblemQuery;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.convert.ProblemConvert;
import com.kelab.problemcenter.dal.dao.ProblemMapper;
import com.kelab.problemcenter.dal.domain.*;
import com.kelab.problemcenter.dal.model.ProblemModel;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.dal.repo.ProblemAttachTagsRepo;
import com.kelab.problemcenter.dal.repo.ProblemRepo;
import com.kelab.problemcenter.dal.repo.ProblemSubmitInfoRepo;
import com.kelab.problemcenter.dal.repo.ProblemTagsRepo;
import com.kelab.problemcenter.support.service.UserCenterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("unchecked")
public class ProblemRepoImpl implements ProblemRepo {

    private ProblemMapper problemMapper;

    private ProblemSubmitInfoRepo problemSubmitInfoRepo;

    private ProblemAttachTagsRepo problemAttachTagsRepo;

    private ProblemTagsRepo problemTagsRepo;

    private RedisCache redisCache;

    private UserCenterService userCenterService;

    @Autowired(required = false)
    public ProblemRepoImpl(ProblemMapper problemMapper,
                           ProblemSubmitInfoRepo problemSubmitInfoRepo,
                           ProblemAttachTagsRepo problemAttachTagsRepo,
                           ProblemTagsRepo problemTagsRepo,
                           RedisCache redisCache,
                           UserCenterService userCenterService) {
        this.problemMapper = problemMapper;
        this.problemSubmitInfoRepo = problemSubmitInfoRepo;
        this.problemAttachTagsRepo = problemAttachTagsRepo;
        this.problemTagsRepo = problemTagsRepo;
        this.redisCache = redisCache;
        this.userCenterService = userCenterService;
    }

    @Override
    public List<ProblemDomain> queryByIds(Context context, List<Integer> ids, ProblemFilterDomain filter) {
        List<ProblemModel> models = redisCache.cacheList(CacheBizName.PROBLEM_INFO, ids, ProblemModel.class, missKeyList -> {
            List<ProblemModel> dbModels = problemMapper.queryByIds(missKeyList);
            if (CollectionUtils.isEmpty(dbModels)) {
                return null;
            }
            return dbModels.stream().collect(Collectors.toMap(ProblemModel::getId, obj -> obj, (v1, v2) -> v2));
        });
        return convertAndFillSubmitInfo(context, models, filter);
    }

    @Override
    @Verify(numberLimit = {"query.page [1, 100000]", "query.rows [1, 100000]"})
    public List<ProblemDomain> queryPage(Context context, ProblemQuery query, ProblemFilterDomain filter) {
        return convertAndFillSubmitInfo(context, problemMapper.queryPage(query), filter);
    }

    @Override
    public Integer queryTotal(ProblemQuery query) {
        return problemMapper.queryTotal(query);
    }

    @Override
    public void save(ProblemDomain record) {
        // 基本信息
        ProblemModel model = ProblemConvert.domainToModel(record);
        problemMapper.save(model);
        record.setId(model.getId());
        // 提交信息
        ProblemSubmitInfoDomain submitInfoDomain = record.getSubmitInfoDomain();
        submitInfoDomain.setProblemId(model.getId());
        problemSubmitInfoRepo.save(submitInfoDomain);
        saveAttachTags(record);
    }

    @Override
    public void delete(List<Integer> ids) {
        problemMapper.delete(ids);
        problemAttachTagsRepo.deleteByProblemIds(ids);
        redisCache.deleteList(CacheBizName.PROBLEM_INFO, ids);
    }

    @Override
    public void update(ProblemDomain record) {
        // 基本信息
        ProblemModel model = ProblemConvert.domainToModel(record);
        problemMapper.update(model);
        // 保存最新标签信息
        problemAttachTagsRepo.deleteByProblemIds(Collections.singletonList(record.getId()));
        saveAttachTags(record);
        redisCache.deleteList(CacheBizName.PROBLEM_INFO, Collections.singletonList(record.getId()));
    }


    @Override
    public List<String> querySource(Integer limit) {
        String cacheData = redisCache.cacheOne(
                CacheBizName.PROBLEM_SOURCE, limit,
                String.class, missKey -> JSON.toJSONString(problemMapper.querySource(missKey)));
        if (StringUtils.isBlank(cacheData)) {
            return Collections.emptyList();
        }
        return JSON.parseArray(cacheData, String.class);
    }

    private void saveAttachTags(ProblemDomain record) {
        // 标签信息
        List<ProblemTagsDomain> tagsDomains = record.getTagsDomains();
        if (!CollectionUtils.isEmpty(tagsDomains)) {
            List<ProblemAttachTagsDomain> attachDomains = new ArrayList<>(tagsDomains.size());
            tagsDomains.forEach(item -> {
                ProblemAttachTagsDomain domain = new ProblemAttachTagsDomain();
                domain.setProblemId(record.getId());
                domain.setTagsId(item.getId());
                attachDomains.add(domain);
            });
            problemAttachTagsRepo.saveList(attachDomains);
        }
    }

    private List<ProblemDomain> convertAndFillSubmitInfo(Context context, List<ProblemModel> models, ProblemFilterDomain filter) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        List<ProblemDomain> result = models.stream().map(ProblemConvert::modelToDomain).collect(Collectors.toList());
        // 填充提交信息
        if (filter != null && filter.isWithSubmitInfo()) {
            List<Integer> probIds = result.stream().map(ProblemDomain::getId).collect(Collectors.toList());
            List<ProblemSubmitInfoDomain> submitInfoDomains = problemSubmitInfoRepo.queryByProbIds(probIds);
            Map<Integer, ProblemSubmitInfoDomain> submitMap = submitInfoDomains
                    .stream().collect(Collectors.toMap(ProblemSubmitInfoDomain::getProblemId, obj -> obj, (v1, v2) -> v2));
            result.forEach(item -> item.setSubmitInfoDomain(submitMap.get(item.getId())));
        }
        if (filter != null && filter.isWithTagsInfo()) {
            fillTagsDomains(result);
        }
        if (filter != null && filter.isWithCreatorInfo()) {
            fillCreatorUserInfo(context, result);
        }
        filterAccessFields(context, result);
        return result;
    }

    /**
     * 过滤非管理员能看的字段
     */
    private void filterAccessFields(Context context, List<ProblemDomain> problemDomains) {
        if (context == null || (!context.getOperatorRoleId().equals(UserRoleConstant.ADMIN) && !context.getOperatorRoleId().equals(UserRoleConstant.TEACHER))) {
            problemDomains.forEach(item -> item.setSpecialJudgeSource(null));
        }
    }

    private void fillTagsDomains(List<ProblemDomain> domains) {
        // 查询关联记录
        List<Integer> probIds = domains.stream().map(ProblemDomain::getId).collect(Collectors.toList());
        List<ProblemAttachTagsDomain> attachRecords = problemAttachTagsRepo.queryByProblemIds(probIds);
        // 有关联记录， 填充tagsInfos
        if (!CollectionUtils.isEmpty(attachRecords)) {
            List<Integer> tagsIds = attachRecords.stream().map(ProblemAttachTagsDomain::getTagsId).collect(Collectors.toList());
            List<ProblemTagsDomain> tagsDomains = problemTagsRepo.queryByIds(tagsIds);
            Map<Integer, ProblemTagsDomain> tagsMap = tagsDomains.stream().collect(Collectors.toMap(ProblemTagsDomain::getId, obj -> obj, (v1, v2) -> v2));
            Map<Integer, List<ProblemTagsDomain>> probAndTagsListMap = attachRecords.stream().collect(
                    Collectors.groupingBy(ProblemAttachTagsDomain::getProblemId, Collectors.mapping(obj -> tagsMap.get(obj.getTagsId()), Collectors.toList())));
            domains.forEach(item -> item.setTagsDomains(probAndTagsListMap.get(item.getId())));
        }
    }

    private void fillCreatorUserInfo(Context context, List<ProblemDomain> domains) {
        // 填充出题人信息
        List<Integer> userIds = domains.stream().map(ProblemDomain::getCreatorId).collect(Collectors.toList());
        List<UserInfo> userInfos = userCenterService.queryByUserIds(context, userIds);
        Map<Integer, UserInfo> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, obj -> obj, (v1, v2) -> v2));
        domains.forEach(item -> item.setCreatorInfo(userInfoMap.get(item.getCreatorId())));
    }
}
