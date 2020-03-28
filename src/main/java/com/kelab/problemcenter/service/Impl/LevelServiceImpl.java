package com.kelab.problemcenter.service.Impl;

import com.google.common.base.Preconditions;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.LevelInfo;
import com.kelab.problemcenter.convert.LevelConvert;
import com.kelab.problemcenter.dal.domain.LevelDomain;
import com.kelab.problemcenter.dal.domain.LevelProblemDomain;
import com.kelab.problemcenter.dal.repo.LevelRepo;
import com.kelab.problemcenter.result.level.LevelProblemResult;
import com.kelab.problemcenter.service.LevelService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LevelServiceImpl implements LevelService {

    private LevelRepo levelRepo;

    public LevelServiceImpl(LevelRepo levelRepo) {
        this.levelRepo = levelRepo;
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
    public LevelProblemResult queryAllProblem(Context context, Integer levelId) {
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
        LevelProblemResult result = new LevelProblemResult();
        result.setId(levelId);
        result.setGrades(new ArrayList<>(levelDomain.getGrade()));
        // 填充段位题目
        for (int i = 1; i <= levelDomain.getGrade(); i++) {
            LevelProblemResult.Grade grade = new LevelProblemResult.Grade();
            List<LevelProblemResult.ProblemResult> problems = gradeProblemMap.getOrDefault(i, Collections.emptyList()).stream().map(LevelConvert::domainToResult).collect(Collectors.toList());
            grade.setName(levelDomain.getName() + i);// 段位名字
            grade.setNumber(problems.size());
            grade.setProblems(problems);
            result.getGrades().add(grade);
        }
        return result;
    }

    @Override
    public void insertProblem(Context context, List<LevelProblemDomain> records) {
        // 强制检验levelId和grade一直
        Integer levelId = records.get(0).getLevelId();
        Integer grade = records.get(0).getGrade();
        for (LevelProblemDomain domain: records) {
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
