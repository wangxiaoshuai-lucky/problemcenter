package com.kelab.problemcenter.service.Impl;

import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemNoteInfo;
import com.kelab.info.problemcenter.query.ProblemNoteQuery;
import com.kelab.problemcenter.convert.ProblemNoteConvert;
import com.kelab.problemcenter.dal.domain.ProblemNoteDomain;
import com.kelab.problemcenter.dal.repo.ProblemNoteRepo;
import com.kelab.problemcenter.service.ProblemNoteService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemNoteServiceImpl implements ProblemNoteService {

    private ProblemNoteRepo problemNoteRepo;

    public ProblemNoteServiceImpl(ProblemNoteRepo problemNoteRepo) {
        this.problemNoteRepo = problemNoteRepo;
    }

    @Override
    public List<ProblemNoteInfo> queryPage(Context context, ProblemNoteQuery query) {
        query.setUserId(context.getOperatorId());
        return convertToTagsInfo(problemNoteRepo.queryPage(query));
    }

    @Override
    public Integer queryTotal(Context context, ProblemNoteQuery query) {
        query.setUserId(context.getOperatorId());
        return problemNoteRepo.queryTotal(query);
    }

    @Override
    public ProblemNoteInfo queryByUserIdAndProbId(Context context, Integer probId) {
        return ProblemNoteConvert.domainToInfo(problemNoteRepo.queryByUserIdAndProbId(context.getOperatorId(), probId));
    }

    @Override
    public void save(Context context, ProblemNoteDomain record) {
        ProblemNoteDomain old = problemNoteRepo.queryByUserIdAndProbId(context.getOperatorId(), record.getProblemId());
        if (old == null) {
            record.setPostTime(System.currentTimeMillis());
            record.setUserId(context.getOperatorId());
            problemNoteRepo.save(record);
        } else {
            record.setId(old.getId());
            this.update(context, record);
        }
    }

    @Override
    public void update(Context context, ProblemNoteDomain record) {
        List<ProblemNoteDomain> old = problemNoteRepo.queryByIds(Collections.singletonList(record.getId()));
        if (CollectionUtils.isEmpty(old) || !old.get(0).getUserId().equals(context.getOperatorId())) {
            throw new IllegalArgumentException("data is not existed or not belong to you");
        }
        record.setPostTime(System.currentTimeMillis());
        record.setUserId(context.getOperatorId());
        problemNoteRepo.update(record);
    }

    @Override
    public void delete(Context context, List<Integer> ids) {
        // 过滤掉是自己的笔记
        List<Integer> deleteIds = problemNoteRepo.queryByIds(ids).stream()
                .filter(item -> item.getUserId().equals(context.getOperatorId()))
                .map(ProblemNoteDomain::getId)
                .collect(Collectors.toList());
        problemNoteRepo.delete(deleteIds);
    }

    private List<ProblemNoteInfo> convertToTagsInfo(List<ProblemNoteDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(ProblemNoteConvert::domainToInfo).collect(Collectors.toList());
    }
}
