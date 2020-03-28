package com.kelab.problemcenter.service.Impl;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemTagsInfo;
import com.kelab.info.problemcenter.query.ProblemTagsQuery;
import com.kelab.problemcenter.convert.ProblemTagsConvert;
import com.kelab.problemcenter.dal.domain.ProblemTagsDomain;
import com.kelab.problemcenter.dal.repo.ProblemTagsRepo;
import com.kelab.problemcenter.service.ProblemTagsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemTagsServiceImpl implements ProblemTagsService {

    private ProblemTagsRepo problemTagsRepo;

    public ProblemTagsServiceImpl(ProblemTagsRepo problemTagsRepo) {
        this.problemTagsRepo = problemTagsRepo;
    }

    @Override
    public PaginationResult<ProblemTagsInfo> queryPage(Context context, ProblemTagsQuery query) {
        PaginationResult<ProblemTagsInfo> result = new PaginationResult<>();
        List<Integer> ids = CommonService.totalIds(query);
        if (ids.size() > 0) {
            // 可以走缓存
            List<ProblemTagsInfo> infos = convertToTagsInfo(problemTagsRepo.queryByIds(ids));
            result.setTotal(infos.size());
            result.setPagingList(infos);
        } else {
            List<ProblemTagsInfo> problemInfos = convertToTagsInfo(problemTagsRepo.queryPage(query));
            result.setTotal(problemTagsRepo.queryTotal(query));
            result.setPagingList(problemInfos);
        }
        return result;
    }

    @Override
    public void save(Context context, ProblemTagsDomain record) {
        List<ProblemTagsDomain> tagsDomains = problemTagsRepo.queryByName(record.getName());
        if (CollectionUtils.isEmpty(tagsDomains)) {
            problemTagsRepo.save(record);
        }
    }

    @Override
    public void update(Context context, ProblemTagsDomain record) {
        problemTagsRepo.update(record);
    }

    @Override
    public void delete(Context context, List<Integer> ids) {
        problemTagsRepo.delete(ids);
    }

    private List<ProblemTagsInfo> convertToTagsInfo(List<ProblemTagsDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(ProblemTagsConvert::domainToInfo).collect(Collectors.toList());
    }
}
