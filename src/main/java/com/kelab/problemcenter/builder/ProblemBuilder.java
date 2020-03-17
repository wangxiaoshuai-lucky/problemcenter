package com.kelab.problemcenter.builder;

import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.problemcenter.convert.ProblemTagsConvert;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemSubmitInfoDomain;
import com.kelab.problemcenter.dal.domain.ProblemTagsDomain;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ProblemBuilder {

    public static ProblemDomain buildNewProblemDomain(ProblemInfo info) {
        ProblemDomain domain = new ProblemDomain();
        domain.setTitle(info.getTitle());
        domain.setDescription(info.getDescription());
        domain.setInput(info.getInput());
        domain.setOutput(info.getOutput());
        domain.setSampleInput(info.getSampleInput());
        domain.setSampleOutput(info.getSampleOutput());
        domain.setHint(info.getHint());
        domain.setTimeLimit(info.getTimeLimit());
        domain.setMemoryLimit(info.getMemoryLimit());
        domain.setSpecialJudge(info.getSpecialJudge());
        domain.setSpecialJudgeSource(info.getSpecialJudgeSource());
        domain.setFrameSource(info.getFrameSource());
        domain.setFrameSourceCompilerId(info.getFrameSourceCompilerId());
        domain.setSource(info.getSource());
        if (!CollectionUtils.isEmpty(info.getTagsInfos())) {
            List<ProblemTagsDomain> tagsDomains = new ArrayList<>(info.getTagsInfos().size());
            info.getTagsInfos().forEach(item -> tagsDomains.add(ProblemTagsConvert.infoToDomain(item)));
            domain.setTagsDomains(tagsDomains);
        }
        ProblemSubmitInfoDomain submitInfoDomain = new ProblemSubmitInfoDomain();
        submitInfoDomain.setSubmitNum(0);
        submitInfoDomain.setAcNum(0);
        domain.setSubmitInfoDomain(submitInfoDomain);
        return domain;
    }
}
