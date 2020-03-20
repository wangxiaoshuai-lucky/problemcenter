package com.kelab.problemcenter.service.Impl;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemSubmitRecordQuery;
import com.kelab.problemcenter.convert.ProblemSubmitRecordConvert;
import com.kelab.problemcenter.dal.domain.ProblemSubmitRecordDomain;
import com.kelab.problemcenter.dal.domain.SubmitRecordFilterDomain;
import com.kelab.problemcenter.dal.repo.ProblemSubmitRecordRepo;
import com.kelab.problemcenter.result.ProblemSubmitRecordInfo;
import com.kelab.problemcenter.service.ProblemSubmitRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemSubmitRecordServiceImpl implements ProblemSubmitRecordService {

    private ProblemSubmitRecordRepo problemSubmitRecordRepo;

    public ProblemSubmitRecordServiceImpl(ProblemSubmitRecordRepo problemSubmitRecordRepo) {
        this.problemSubmitRecordRepo = problemSubmitRecordRepo;
    }

    @Override
    public PaginationResult<ProblemSubmitRecordInfo> queryPage(Context context, ProblemSubmitRecordQuery query) {
        PaginationResult<ProblemSubmitRecordInfo> result = new PaginationResult<>();
        List<ProblemSubmitRecordInfo> infos = convertToInfo(problemSubmitRecordRepo.queryPage(context, query, new SubmitRecordFilterDomain(true, true)));
        result.setPagingList(infos);
        result.setTotal(problemSubmitRecordRepo.queryTotal(query));
        return result;
    }

    @Override
    public Integer judgeCount(Context context) {
        return problemSubmitRecordRepo.queryTotal(new ProblemSubmitRecordQuery());
    }

    private List<ProblemSubmitRecordInfo> convertToInfo(List<ProblemSubmitRecordDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(ProblemSubmitRecordConvert::domainToInfo).collect(Collectors.toList());
    }
}
