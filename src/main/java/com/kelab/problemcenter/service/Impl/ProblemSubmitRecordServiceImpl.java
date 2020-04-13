package com.kelab.problemcenter.service.Impl;

import com.google.common.base.Preconditions;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.SingleResult;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.base.constant.UserRoleConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemSubmitRecordInfo;
import com.kelab.info.problemcenter.query.ProblemSubmitRecordQuery;
import com.kelab.info.problemcenter.vo.JudgeResult;
import com.kelab.info.problemcenter.vo.JudgeTask;
import com.kelab.info.problemcenter.vo.ResultCase;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.problemcenter.config.AppSetting;
import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.constant.enums.MarkType;
import com.kelab.problemcenter.constant.enums.ProblemJudgeStatus;
import com.kelab.problemcenter.convert.ProblemSubmitRecordConvert;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemSubmitRecordDomain;
import com.kelab.problemcenter.dal.domain.ProblemUserMarkDomain;
import com.kelab.problemcenter.dal.domain.SubmitRecordFilterDomain;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.dal.repo.ProblemRepo;
import com.kelab.problemcenter.dal.repo.ProblemSubmitInfoRepo;
import com.kelab.problemcenter.dal.repo.ProblemSubmitRecordRepo;
import com.kelab.problemcenter.dal.repo.ProblemUserMarkRepo;
import com.kelab.problemcenter.result.MilestoneResult;
import com.kelab.problemcenter.result.SubmitResult;
import com.kelab.problemcenter.result.UserSubmitResult;
import com.kelab.problemcenter.service.ProblemSubmitRecordService;
import com.kelab.problemcenter.support.service.UserCenterService;
import com.kelab.util.uuid.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemSubmitRecordServiceImpl implements ProblemSubmitRecordService {

    private static final int[] AC_MILESTONE = new int[]{
            1, 10, 20, 50, 100, 150, 200, 300, 400, 600, 800, 1000, 1300, 1600, 2000, 2500
    };

    // 最小间隔时间为 3s
    private static final long Minimum_Interval_Time = 3000L;

    private ProblemSubmitRecordRepo problemSubmitRecordRepo;

    private ProblemRepo problemRepo;

    private ProblemSubmitInfoRepo problemSubmitInfoRepo;

    private ProblemUserMarkRepo problemUserMarkRepo;

    private UserCenterService userCenterService;

    private RedisCache redisCache;

    private RestTemplate restTemplate;

    public ProblemSubmitRecordServiceImpl(ProblemSubmitRecordRepo problemSubmitRecordRepo,
                                          ProblemRepo problemRepo,
                                          ProblemSubmitInfoRepo problemSubmitInfoRepo,
                                          ProblemUserMarkRepo problemUserMarkRepo,
                                          UserCenterService userCenterService,
                                          RedisCache redisCache,
                                          RestTemplate restTemplate) {
        this.problemSubmitRecordRepo = problemSubmitRecordRepo;
        this.problemRepo = problemRepo;
        this.problemSubmitInfoRepo = problemSubmitInfoRepo;
        this.problemUserMarkRepo = problemUserMarkRepo;
        this.userCenterService = userCenterService;
        this.redisCache = redisCache;
        this.restTemplate = restTemplate;
    }

    @Override
    public PaginationResult<ProblemSubmitRecordInfo> queryPage(Context context, ProblemSubmitRecordQuery query) {
        PaginationResult<ProblemSubmitRecordInfo> result = new PaginationResult<>();
        List<ProblemSubmitRecordInfo> infos = convertToInfo(context, problemSubmitRecordRepo.queryPage(context, query, new SubmitRecordFilterDomain(true, true)));
        result.setPagingList(infos);
        result.setTotal(problemSubmitRecordRepo.queryTotal(query));
        return result;
    }

    @Override
    public SubmitResult submit(Context context, ProblemSubmitRecordDomain record) {
        fillSubmitRecord(context, record);
        SubmitResult result = new SubmitResult();
        // 判断是否频繁操作
        if (isFrequently(record.getUserId())) {
            result.setStatus(StatusMsgConstant.SUBMIT_FREQUENTLY_ERROR);
            return result;
        }
        List<ProblemDomain> problems = problemRepo.queryByIds(context, Collections.singletonList(record.getProblemId()), null);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(problems));
        problemSubmitRecordRepo.saveSubmitRecord(record);
        result.setSubmitId(record.getId());
        result.setStatus(StatusMsgConstant.SUCCESS);
        sendJudgeTask(record, problems.get(0));
        return result;
    }

    private void sendJudgeTask(ProblemSubmitRecordDomain record, ProblemDomain problem) {
        JudgeTask task = new JudgeTask();
        task.setProId(record.getProblemId());
        task.setJudgeId(record.getCompilerId());
        task.setSrc(record.getSource());
        task.setTimeLimit(problem.getTimeLimit());
        task.setMemoryLimit(problem.getMemoryLimit());
        String uuid = UuidUtil.genUUID();
        redisCache.set(CacheBizName.JUDGE_KEY, String.valueOf(record.getId()), uuid);
        task.setCallBack(String.format("%s?id=%d&key=%s", AppSetting.judgeCallback, record.getId(), uuid));
        ResponseEntity<String> response = restTemplate.postForEntity(AppSetting.judgeServiceUrl, task, String.class);
        Preconditions.checkArgument(response.getStatusCode() == HttpStatus.OK, "发送判题失败");
        Preconditions.checkArgument("OK".equals(response.getBody()), "发送判题失败");
    }

    @Override
    public void judgeCallback(Context context, Integer id, String key, JudgeResult result) {
        Preconditions.checkArgument(key.equals(redisCache.get(CacheBizName.JUDGE_KEY, String.valueOf(id))), "权限有误");
        List<ProblemSubmitRecordDomain> submitRecords = problemSubmitRecordRepo.queryByIds(context, Collections.singletonList(id), null);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(submitRecords));
        ProblemSubmitRecordDomain record = submitRecords.get(0);
        // 判题出现错误
        if (StringUtils.isNotBlank(result.getGlobalMsg())) {
            record.setErrorMessage(result.getGlobalMsg());
            record.setStatus(ProblemJudgeStatus.CE);
            problemSubmitRecordRepo.updateSubmitRecord(record);
            updateSubmitInfo(record);
            return;
        }
        for (ResultCase single : result.getResult()) {
            record.setTimeUsed(single.getTimeUsed());
            record.setMemoryUsed(single.getMemoryUsed());
            if (single.getStatus() != ProblemJudgeStatus.AC.value()) {
                record.setStatus(ProblemJudgeStatus.valueOf(single.getStatus()));
                break;
            } else {
                record.setStatus(ProblemJudgeStatus.AC);
            }
        }
        // 已经赋值好了
        if (record.getStatus() != ProblemJudgeStatus.WAITING) {
            problemSubmitRecordRepo.updateSubmitRecord(record);
            updateSubmitInfo(record);
        }
    }

    private void updateSubmitInfo(ProblemSubmitRecordDomain record) {
        // 更新题目的提交情况
        problemSubmitInfoRepo.updateByProbId(record.getProblemId(), record.getStatus() == ProblemJudgeStatus.AC);
        // 看之前的 ac 或者 challenge 记录
        List<ProblemUserMarkDomain> records = problemUserMarkRepo.queryByUserIdAndProIdsAndTypes(record.getUserId(),
                Collections.singletonList(record.getProblemId()), Arrays.asList(MarkType.AC, MarkType.CHALLENGING));
        // 最多只有一个
        if (records.size() == 0) {
            ProblemUserMarkDomain newRecord = new ProblemUserMarkDomain();
            newRecord.setProblemId(record.getProblemId());
            newRecord.setUserId(record.getUserId());
            newRecord.setMarkType(record.getStatus() == ProblemJudgeStatus.AC ? MarkType.AC : MarkType.CHALLENGING);
            newRecord.setSubmitId(record.getId());
            problemUserMarkRepo.save(newRecord);
            // 第一次 ac 或者 challenge
            userCenterService.judgeCallback(null, record.getUserId(), record.getStatus() == ProblemJudgeStatus.AC);
        } else {
            ProblemUserMarkDomain oldRecord = records.get(0);
            // 重复 ac 或者 challenge 更新关联id
            if (record.getStatus() == ProblemJudgeStatus.AC && oldRecord.getMarkType() == MarkType.AC
                    || record.getStatus() != ProblemJudgeStatus.AC && oldRecord.getMarkType() == MarkType.CHALLENGING) {
                oldRecord.setSubmitId(record.getId());
                problemUserMarkRepo.update(oldRecord);
                // 都算用户没有ac
                userCenterService.judgeCallback(null,record.getUserId(), false);
            } else if (record.getStatus() == ProblemJudgeStatus.AC){
                // 从 challenge 到 ac
                problemUserMarkRepo.delete(record.getUserId(), record.getProblemId(), MarkType.CHALLENGING);
                ProblemUserMarkDomain newRecord = new ProblemUserMarkDomain();
                newRecord.setProblemId(record.getProblemId());
                newRecord.setUserId(record.getUserId());
                newRecord.setMarkType(MarkType.AC);
                newRecord.setSubmitId(record.getId());
                problemUserMarkRepo.save(newRecord);
                userCenterService.judgeCallback(null, record.getUserId(), true);
            }
        }
    }

    @Override
    public SingleResult<ProblemSubmitRecordInfo> querySubmitDetail(Context context, Integer submitId) {
        List<ProblemSubmitRecordDomain> domains = problemSubmitRecordRepo.queryByIds(context, Collections.singletonList(submitId),
                new SubmitRecordFilterDomain(true, true));
        List<ProblemSubmitRecordInfo> infos = convertToInfo(context, domains);
        if (infos.size() == 0) {
            return null;
        }
        SingleResult<ProblemSubmitRecordInfo> result = new SingleResult<>();
        result.setObj(infos.get(0));
        return result;
    }

    @Override
    public Integer judgeCount(Context context) {
        return problemSubmitRecordRepo.queryTotal(new ProblemSubmitRecordQuery());
    }

    @Override
    public Map<String, OnlineStatisticResult> countDay(Context context, Long startTime, Long endTime) {
        return problemSubmitRecordRepo.countDay(startTime, endTime);
    }

    @Override
    public List<MilestoneResult> queryMilestone(Context context) {
        List<MilestoneResult> results = new ArrayList<>();
        for (Integer num : AC_MILESTONE) {
            MilestoneResult single = problemSubmitRecordRepo.queryUserStatus(context.getOperatorId(), ProblemJudgeStatus.AC, num);
            if (single != null) {
                results.add(single);
            } else {
                // 最新的提交ac记录
                single = getLastAcRecord(context);
                if (single != null) {
                    results.add(single);
                }
                break;
            }
        }
        // fillTitle
        fillTitle(results);
        return results;
    }

    @Override
    public List<UserSubmitResult> userSubmit(Context context, Integer userId) {
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();
        Map<Integer, Integer> map = problemSubmitRecordRepo.queryCountWeek(userId, startTime, endTime).stream().collect(Collectors.toMap(UserSubmitResult::getName, UserSubmitResult::getValue));
        List<UserSubmitResult> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            UserSubmitResult single = new UserSubmitResult();
            int time = calendar.get(Calendar.DAY_OF_WEEK);
            single.setName(time);
            Integer value = map.get(time);
            if (value != null) {
                single.setValue(value);
            } else {
                single.setValue(0);
            }
            result.add(single);
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        return result;
    }

    private MilestoneResult getLastAcRecord(Context context) {
        ProblemSubmitRecordQuery query = new ProblemSubmitRecordQuery();
        query.setStatus(ProblemJudgeStatus.AC.value());
        query.setUserId(context.getOperatorId());
        Integer integer = problemSubmitRecordRepo.queryTotal(query);
        if (integer == 0) {
            return null;
        }
        return problemSubmitRecordRepo.queryUserStatus(context.getOperatorId(), ProblemJudgeStatus.AC, integer);
    }

    private void fillTitle(List<MilestoneResult> milestoneResults) {
        // 填充title
        if (CollectionUtils.isEmpty(milestoneResults)) {
            return;
        }
        Map<Integer, ProblemDomain> problemMap = problemRepo.queryProblemMapByIds(null,
                milestoneResults.stream().map(MilestoneResult::getProblemId).collect(Collectors.toList()),
                null);
        milestoneResults.forEach(item -> item.setProblemTitle(problemMap.getOrDefault(item.getProblemId(), new ProblemDomain()).getTitle()));
    }


    /**
     * 检查是否提交太频繁
     * 如果正常则记录此次提交时间
     */
    private boolean isFrequently(Integer userId) {
        String cache = redisCache.get(CacheBizName.USER_LAST_SEND_TIME, String.valueOf(userId));
        if (cache != null) {
            long lastTime = Long.parseLong(cache);
            if (System.currentTimeMillis() - lastTime < Minimum_Interval_Time) {
                return true;
            }
        }
        redisCache.set(CacheBizName.USER_LAST_SEND_TIME, String.valueOf(userId), String.valueOf(System.currentTimeMillis()));
        return false;
    }

    /**
     * 填充新提交信息
     */
    private void fillSubmitRecord(Context context, ProblemSubmitRecordDomain record) {
        record.setUserId(context.getOperatorId());
        record.setCodeLength(record.getSource().length());
        record.setStatus(ProblemJudgeStatus.WAITING);
        record.setTimeUsed(-1);
        record.setMemoryUsed(-1);
        record.setSubmitTime(System.currentTimeMillis());
    }

    private List<ProblemSubmitRecordInfo> convertToInfo(Context context, List<ProblemSubmitRecordDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        filterAccessFields(context, domains);
        return domains.stream().map(ProblemSubmitRecordConvert::domainToInfo).collect(Collectors.toList());
    }

    /**
     * 过滤具体信息字段
     */
    private void filterAccessFields(Context context, List<ProblemSubmitRecordDomain> domains) {
        if (!context.getOperatorRoleId().equals(UserRoleConstant.ADMIN) &&
                !context.getOperatorRoleId().equals(UserRoleConstant.TEACHER) &&
                !context.getOperatorRoleId().equals(UserRoleConstant.ACM_ER)) {
            domains.forEach(item -> {
                if (!item.getUserId().equals(context.getOperatorId())) {
                    item.setErrorMessage(null);
                    item.setSource(null);
                }
            });
        }
    }
}
