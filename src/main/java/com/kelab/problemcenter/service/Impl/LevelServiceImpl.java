package com.kelab.problemcenter.service.Impl;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.LevelInfo;
import com.kelab.problemcenter.convert.LevelConvert;
import com.kelab.problemcenter.dal.domain.LevelDomain;
import com.kelab.problemcenter.dal.repo.LevelRepo;
import com.kelab.problemcenter.service.LevelService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
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



    private List<LevelInfo> convertToInfo(List<LevelDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(LevelConvert::domainToInfo).collect(Collectors.toList());
    }
}
