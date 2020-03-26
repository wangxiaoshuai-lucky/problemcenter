package com.kelab.problemcenter.service.Impl;

import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemUserMarkInfo;
import com.kelab.problemcenter.constant.enums.MarkType;
import com.kelab.problemcenter.convert.ProblemUserMarkConvert;
import com.kelab.problemcenter.dal.domain.ProblemUserMarkDomain;
import com.kelab.problemcenter.dal.repo.ProblemUserMarkRepo;
import com.kelab.problemcenter.service.ProblemUserMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemUserMarkServiceImpl implements ProblemUserMarkService {

    private final ProblemUserMarkRepo problemUserMarkRepo;

    @Autowired(required = false)
    public ProblemUserMarkServiceImpl(ProblemUserMarkRepo problemUserMarkRepo) {
        this.problemUserMarkRepo = problemUserMarkRepo;
    }

    @Override
    public Map<MarkType, List<ProblemUserMarkInfo>> queryAcAndChallenging(Context context, Integer userId) {
        Map<MarkType, List<ProblemUserMarkInfo>> result = new HashMap<>();
        result.put(MarkType.AC, Collections.emptyList());
        result.put(MarkType.CHALLENGING, Collections.emptyList());
        List<ProblemUserMarkDomain> allRecords = problemUserMarkRepo.queryByUserIdAndTypes(userId, Arrays.asList(MarkType.AC, MarkType.CHALLENGING));
        if (CollectionUtils.isEmpty(allRecords)) {
            return result;
        }
        Map<MarkType, List<ProblemUserMarkDomain>> collect = allRecords.stream().collect(Collectors.groupingBy(ProblemUserMarkDomain::getMarkType));
        if (collect.containsKey(MarkType.AC)) {
            result.put(MarkType.AC, convertDomainsToInfos(collect.get(MarkType.AC)));
        }
        if (collect.containsKey(MarkType.CHALLENGING)) {
            result.put(MarkType.CHALLENGING, convertDomainsToInfos(collect.get(MarkType.CHALLENGING)));
        }
        return result;
    }

    @Override
    public List<ProblemUserMarkInfo> collect(Context context) {
        List<ProblemUserMarkDomain> collects = problemUserMarkRepo.queryByUserIdAndTypes(context.getOperatorId(), Collections.singletonList(MarkType.COLLECT));
        return convertDomainsToInfos(collects);
    }

    @Override
    public void deleteOrSave(Context context, ProblemUserMarkInfo record) {
        List<ProblemUserMarkDomain> old = problemUserMarkRepo.queryByUserIdAndProIdsAndTypes(record.getUserId(),
                Collections.singletonList(record.getProblemId()),
                Collections.singletonList(MarkType.COLLECT));
        if (CollectionUtils.isEmpty(old)) {
            // 添加收藏
            problemUserMarkRepo.save(context.getOperatorId(), record.getProblemId(), MarkType.COLLECT);
        } else {
            // 删除收藏
            problemUserMarkRepo.delete(context.getOperatorId(), record.getProblemId(), MarkType.COLLECT);
        }
    }

    private List<ProblemUserMarkInfo> convertDomainsToInfos(List<ProblemUserMarkDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(ProblemUserMarkConvert::domainToVo).collect(Collectors.toList());
    }
}
