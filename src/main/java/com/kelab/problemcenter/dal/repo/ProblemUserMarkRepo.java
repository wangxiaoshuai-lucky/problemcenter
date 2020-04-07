package com.kelab.problemcenter.dal.repo;

import com.kelab.problemcenter.constant.enums.MarkType;
import com.kelab.problemcenter.dal.domain.ProblemUserMarkDomain;

import java.util.List;

public interface ProblemUserMarkRepo {


    List<ProblemUserMarkDomain> queryByUserIdAndTypes(Integer userId, List<MarkType> types);

    List<ProblemUserMarkDomain> queryByUserIdAndProIdsAndTypes(Integer userId, List<Integer> problemIds, List<MarkType> types);

    void save(Integer userId, Integer problemId, MarkType markType);

    void delete(Integer userId, Integer problemId, MarkType markType);

    List<ProblemUserMarkDomain> queryByUserIdsAndProbIdsAndEndTime(List<Integer> userIds, List<Integer> probIds, List<MarkType> types, Long endTime);
}
