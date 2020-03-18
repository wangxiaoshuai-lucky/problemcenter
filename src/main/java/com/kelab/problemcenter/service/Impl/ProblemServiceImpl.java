package com.kelab.problemcenter.service.Impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.constant.UserRoleConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.info.problemcenter.query.ProblemQuery;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.problemcenter.constant.enums.MarkType;
import com.kelab.problemcenter.constant.enums.ProblemStatus;
import com.kelab.problemcenter.convert.ProblemConvert;
import com.kelab.problemcenter.dal.domain.ProblemAttachTagsDomain;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemTagsDomain;
import com.kelab.problemcenter.dal.domain.ProblemUserMarkDomain;
import com.kelab.problemcenter.dal.repo.ProblemAttachTagsRepo;
import com.kelab.problemcenter.dal.repo.ProblemRepo;
import com.kelab.problemcenter.dal.repo.ProblemTagsRepo;
import com.kelab.problemcenter.dal.repo.ProblemUserMarkRepo;
import com.kelab.problemcenter.service.ProblemService;
import com.kelab.problemcenter.support.ContextLogger;
import com.kelab.problemcenter.support.service.UserCenterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemServiceImpl implements ProblemService {

    private ProblemRepo problemRepo;

    private ContextLogger contextLogger;

    private ProblemTagsRepo problemTagsRepo;

    private ProblemUserMarkRepo problemUserMarkRepo;

    private ProblemAttachTagsRepo problemAttachTagsRepo;

    private UserCenterService userCenterService;

    public ProblemServiceImpl(ProblemRepo problemRepo,
                              ContextLogger contextLogger,
                              ProblemTagsRepo problemTagsRepo,
                              ProblemUserMarkRepo problemUserMarkRepo,
                              ProblemAttachTagsRepo problemAttachTagsRepo,
                              UserCenterService userCenterService) {
        this.problemRepo = problemRepo;
        this.contextLogger = contextLogger;
        this.problemTagsRepo = problemTagsRepo;
        this.problemUserMarkRepo = problemUserMarkRepo;
        this.problemAttachTagsRepo = problemAttachTagsRepo;
        this.userCenterService = userCenterService;
    }

    @Override
    @Transactional
    public void saveProblem(Context context, ProblemDomain record) {
        if (context.getOperatorRoleId().equals(UserRoleConstant.ADMIN)) {
            record.setStatus(ProblemStatus.PASS_REVIEW);
        } else {
            record.setStatus(ProblemStatus.IN_REVIEW);
        }
        record.setCreatorId(context.getOperatorId());
        problemRepo.save(record);
        contextLogger.info(context, "新添加题目%s", JSON.toJSONString(record));
    }

    @Override
    public void deleteProblems(Context context, List<Integer> probIds) {
        List<ProblemDomain> oldProblems = problemRepo.queryByIds(probIds, true);
        problemRepo.delete(probIds);
        contextLogger.info(context, "删除题目%s", JSON.toJSONString(oldProblems));
    }

    @Override
    public void updateProblem(Context context, ProblemDomain record) {
        List<ProblemDomain> oldProblems = problemRepo.queryByIds(Collections.singletonList(record.getId()), false);
        problemRepo.update(record);
        contextLogger.info(context, "更新题目，原信息:%s", JSON.toJSONString(oldProblems));
    }

    @Override
    public Integer problemTotal(Context context) {
        return problemRepo.queryTotal(new ProblemQuery());
    }

    @Override
    public List<String> querySource(Context context, Integer limit) {
        if (limit == null) {
            limit = 50;
        }
        return problemRepo.querySource(limit);
    }

    @Override
    public PaginationResult<ProblemInfo> queryPage(Context context, ProblemQuery query) {
        PaginationResult<ProblemInfo> result = new PaginationResult<>();
        List<Integer> totalIds = CommonService.totalIds(query);
        if (totalIds.size() > 0) {
            // 可以走缓存
            List<ProblemInfo> problemInfos = fillAndConvertToInfo(context, problemRepo.queryByIds(totalIds, true));
            result.setTotal(problemInfos.size());
            result.setPagingList(problemInfos);
        } else {
            // 查询审核通过的题目
            // 标签查询
            if (StringUtils.isNotBlank(query.getTagsName())) {
                List<Integer> probIds = problemAttachTagsRepo.queryByTagsName(query.getTagsName())
                        .stream().map(ProblemAttachTagsDomain::getProblemId).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(probIds)) {
                    query.setIds(probIds);
                } else {
                    result.setTotal(0);
                    result.setPagingList(Collections.emptyList());
                    return result;
                }
            }
            List<ProblemInfo> problemInfos = fillAndConvertToInfo(context, problemRepo.queryPage(query));
            result.setTotal(problemRepo.queryTotal(query));
            result.setPagingList(problemInfos);
        }
        return result;
    }

    /**
     * 填充关联 tags 信息、出题人信息、是否 ac 或者 收藏
     */
    private List<ProblemInfo> fillAndConvertToInfo(Context context, List<ProblemDomain> problemDomains) {
        if (CollectionUtils.isEmpty(problemDomains)) {
            return Collections.emptyList();
        }
        filterAccessFields(context, problemDomains);
        // 填充标签信息
        fillTagsDomains(context, problemDomains);
        // 填充出题人信息
        fillCreatorUserInfo(context, problemDomains);
        // 填充是否 ac 或者 收藏
        fillMarkInfo(context, problemDomains);
        // 转换模型
        List<ProblemInfo> result = new ArrayList<>(problemDomains.size());
        problemDomains.forEach(item -> result.add(ProblemConvert.domainToInfo(item)));
        return result;
    }

    private void fillMarkInfo(Context context, List<ProblemDomain> domains) {
        if (UserRoleConstant.NOT_LOGIN == context.getOperatorRoleId()) {
            return;
        }
        List<Integer> probIds = domains.stream().map(ProblemDomain::getId).collect(Collectors.toList());
        List<ProblemUserMarkDomain> markRecords = problemUserMarkRepo.queryByUserIdAndProIdsAndTypes(context.getOperatorId(),
                probIds,
                Arrays.asList(MarkType.CHALLENGING, MarkType.AC, MarkType.COLLECT));
        if (!CollectionUtils.isEmpty(markRecords)) {
            Map<Integer, boolean[]> probMarkMap = new HashMap<>();
            markRecords.forEach(item -> {
                boolean[] marks = probMarkMap.computeIfAbsent(item.getProblemId(), k -> new boolean[3]);
                switch (item.getMarkType()) {
                    case AC:
                        marks[0] = true;
                        break;
                    case CHALLENGING:
                        marks[1] = true;
                        break;
                    case COLLECT:
                        marks[2] = true;
                        break;
                }
            });
            domains.forEach(item -> {
                boolean[] marks = probMarkMap.get(item.getId());
                if (marks != null && marks[0]) {
                    item.setUserStatus(MarkType.AC);
                }
                if (marks != null && marks[1]) {
                    item.setUserStatus(MarkType.CHALLENGING);
                }
                if (marks != null && marks[2]) {
                    item.setUserCollect(true);
                }
            });
        }
    }

    private void fillTagsDomains(Context context, List<ProblemDomain> domains) {
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

    /**
     * 过滤非管理员能看的字段
     */
    private void filterAccessFields(Context context, List<ProblemDomain> problemDomains) {
        if (!context.getOperatorRoleId().equals(UserRoleConstant.ADMIN) && !context.getOperatorRoleId().equals(UserRoleConstant.TEACHER)) {
            problemDomains.forEach(item -> item.setSpecialJudgeSource(null));
        }
    }
}
