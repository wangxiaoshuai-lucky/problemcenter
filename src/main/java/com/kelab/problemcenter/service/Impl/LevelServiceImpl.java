package com.kelab.problemcenter.service.Impl;

import com.google.common.base.Preconditions;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.LevelInfo;
import com.kelab.problemcenter.constant.enums.MarkType;
import com.kelab.problemcenter.convert.LevelConvert;
import com.kelab.problemcenter.dal.domain.LevelDomain;
import com.kelab.problemcenter.dal.domain.LevelProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemUserMarkDomain;
import com.kelab.problemcenter.dal.repo.LevelRepo;
import com.kelab.problemcenter.dal.repo.ProblemUserMarkRepo;
import com.kelab.problemcenter.result.level.LevelProblemAdminResult;
import com.kelab.problemcenter.result.level.LevelProblemUserResult;
import com.kelab.problemcenter.result.level.ProblemResult;
import com.kelab.problemcenter.service.LevelService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LevelServiceImpl implements LevelService {

    private LevelRepo levelRepo;

    private ProblemUserMarkRepo problemUserMarkRepo;

    public LevelServiceImpl(LevelRepo levelRepo,
                            ProblemUserMarkRepo problemUserMarkRepo) {
        this.levelRepo = levelRepo;
        this.problemUserMarkRepo = problemUserMarkRepo;
    }

    @Override
    public PaginationResult<LevelInfo> queryAllLevel(Context context) {
        List<LevelInfo> infos = convertToInfo(levelRepo.queryAll());
        PaginationResult<LevelInfo> result = new PaginationResult<>();
        result.setPagingList(infos);
        result.setTotal(infos.size());
        return result;
    }

    @Override
    public void saveLevel(Context context, LevelDomain record) {
        levelRepo.save(record);
    }

    @Override
    public void updateLevel(Context context, LevelDomain record) {
        levelRepo.update(record);
    }

    @Override
    public void deleteLevel(Context context, List<Integer> ids) {
        levelRepo.delete(ids);
    }

    @Override
    public LevelProblemAdminResult queryAllProblem(Context context, Integer levelId) {
        // 段位不多 在获取题目之前肯定调用过获取所有段位接口
        // 获取所有可以走缓存
        List<LevelDomain> levelDomains = levelRepo.queryAll().stream().filter(item -> item.getId().equals(levelId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(levelDomains)) {
            throw new IllegalArgumentException("level is not existed");
        }
        LevelDomain levelDomain = levelDomains.get(0);
        // 按照小段位分组
        List<LevelProblemDomain> levelProblemDomains = levelRepo.queryLevelProblemByLevelId(levelId);
        Map<Integer, List<LevelProblemDomain>> gradeProblemMap = levelProblemDomains.stream().collect(Collectors.groupingBy(LevelProblemDomain::getGrade));
        LevelProblemAdminResult result = new LevelProblemAdminResult();
        result.setId(levelId);
        result.setName(levelDomain.getName());
        result.setGrades(new ArrayList<>(levelDomain.getGrade()));
        // 填充段位题目
        for (int i = 1; i <= levelDomain.getGrade(); i++) {
            LevelProblemAdminResult.Grade grade = new LevelProblemAdminResult.Grade();
            List<ProblemResult> problems = gradeProblemMap.getOrDefault(i, Collections.emptyList()).stream().map(LevelConvert::domainToResult).collect(Collectors.toList());
            grade.setName(levelDomain.getName() + i);// 段位名字
            grade.setNumber(problems.size());
            grade.setProblems(problems);
            result.getGrades().add(grade);
        }
        return result;
    }

    /**
     * success 代表是否全部A
     * status : 0 不能做，1 可挑战 2 管理员没有添加题目
     */
    @Override
    public LevelProblemUserResult queryAllGradeInfo(Context context, Integer levelId) {
        LevelProblemAdminResult allProblem = this.queryAllProblem(context, levelId);
        LevelProblemUserResult result = new LevelProblemUserResult();
        result.setName(allProblem.getName());
        result.setGrade(allProblem.getGrades().size());
        result.setDetails(new ArrayList<>(result.getGrade()));
        for (LevelProblemAdminResult.Grade grade : allProblem.getGrades()) {
            // 初始化都不能挑战
            LevelProblemUserResult.Detail detail = new LevelProblemUserResult.Detail();
            detail.setName(grade.getName());
            detail.setSuccess(false);
            result.getDetails().add(detail);
        }
        // 收集所有的题目id
        List<Integer> proIds = getAllProblemIds(allProblem);
        // 之前的段位题目列表
        List<LevelProblemDomain> blowProblems = levelRepo.queryAllBelowTheLevel(levelId);
        List<Integer> blowProIds = blowProblems.stream().map(LevelProblemDomain::getProId).collect(Collectors.toList());
        proIds.addAll(blowProIds);
        if (CollectionUtils.isEmpty(proIds)) {
            // 未添加题目
            result.getDetails().forEach(item -> item.setStatus(2));
            return result;
        }
        List<ProblemUserMarkDomain> acs = problemUserMarkRepo.queryByUserIdAndProIdsAndTypes(context.getOperatorId(), proIds, Collections.singletonList(MarkType.AC));
        Set<Integer> acSet = acs.stream().map(ProblemUserMarkDomain::getProblemId).collect(Collectors.toSet());
        // 检测之前的题目是否ac
        if (!CollectionUtils.isEmpty(blowProIds)) {
            List<Integer> acProIds = blowProIds.stream().filter(acSet::contains).collect(Collectors.toList());
            if (acProIds.size() < blowProIds.size()) {// 没有ac完
                // 不能看 没到等级
                result.getDetails().forEach(item -> item.setStatus(0));
                return result;
            }
        }
        for (int i = 0; i < result.getGrade(); i++) {
            LevelProblemUserResult.Detail detail = result.getDetails().get(i);
            LevelProblemAdminResult.Grade grade = allProblem.getGrades().get(i);
            List<ProblemResult> problems = grade.getProblems();
            detail.setProblems(problems);
            if (CollectionUtils.isEmpty(problems)) {
                detail.setStatus(2);// 管理员未添加题目
                continue;
            }
            detail.setStatus(1);// 可挑战
            for (ProblemResult problem : problems) {
                problem.setAc(acSet.contains(problem.getProId()));
            }
            List<ProblemResult> acProblem = problems.stream().filter(ProblemResult::isAc).collect(Collectors.toList());
            if (acProblem.size() == problems.size()) {
                detail.setSuccess(true);// 全部ac
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * 查询所有的题目id
     */
    private List<Integer> getAllProblemIds(LevelProblemAdminResult allProblem) {
        List<Integer> ids = new ArrayList<>();
        for (LevelProblemAdminResult.Grade grade : allProblem.getGrades()) {
            ids.addAll(grade.getProblems().stream().map(ProblemResult::getProId).collect(Collectors.toList()));
        }
        return ids;
    }

    @Override
    public void insertProblem(Context context, List<LevelProblemDomain> records) {
        // 强制检验levelId和grade一直
        Integer levelId = records.get(0).getLevelId();
        Integer grade = records.get(0).getGrade();
        for (LevelProblemDomain domain : records) {
            Preconditions.checkNotNull(domain.getProId());
            Preconditions.checkNotNull(domain.getGrade());
            Preconditions.checkNotNull(domain.getLevelId());
            Preconditions.checkArgument(domain.getGrade().equals(grade), "grade 不一致");
            Preconditions.checkArgument(domain.getLevelId().equals(levelId), "level 不一致");
        }
        levelRepo.insertProblem(records);
    }

    private List<LevelInfo> convertToInfo(List<LevelDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(LevelConvert::domainToInfo).collect(Collectors.toList());
    }
}
