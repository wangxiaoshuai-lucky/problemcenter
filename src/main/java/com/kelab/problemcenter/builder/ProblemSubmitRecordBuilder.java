package com.kelab.problemcenter.builder;

import com.kelab.info.problemcenter.info.ProblemSubmitRecordInfo;
import com.kelab.problemcenter.dal.domain.ProblemSubmitRecordDomain;

public class ProblemSubmitRecordBuilder {

    public static ProblemSubmitRecordDomain buildNewSubmitRecord(ProblemSubmitRecordInfo info) {
        ProblemSubmitRecordDomain domain = new ProblemSubmitRecordDomain();
        domain.setCompilerId(info.getCompilerId());
        domain.setProblemId(info.getProblemId());
        domain.setSource(info.getSource());
        return domain;
    }
}
