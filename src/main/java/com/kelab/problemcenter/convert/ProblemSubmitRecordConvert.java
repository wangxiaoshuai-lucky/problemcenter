package com.kelab.problemcenter.convert;

import com.kelab.problemcenter.constant.enums.ProblemJudgeStatus;
import com.kelab.problemcenter.dal.domain.ProblemSubmitRecordDomain;
import com.kelab.problemcenter.dal.model.ProblemSubmitRecordModel;
import com.kelab.info.problemcenter.info.ProblemSubmitRecordInfo;
import org.springframework.beans.BeanUtils;

public class ProblemSubmitRecordConvert {

    public static ProblemSubmitRecordDomain modelToDomain(ProblemSubmitRecordModel model) {
        if (model == null) {
            return null;
        }
        ProblemSubmitRecordDomain domain = new ProblemSubmitRecordDomain();
        BeanUtils.copyProperties(model, domain);
        domain.setStatus(ProblemJudgeStatus.valueOf(model.getStatus()));
        return domain;
    }

    public static ProblemSubmitRecordModel domainToModel(ProblemSubmitRecordDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemSubmitRecordModel model = new ProblemSubmitRecordModel();
        BeanUtils.copyProperties(domain, model);
        model.setStatus(domain.getStatus().value());
        return model;
    }

    public static ProblemSubmitRecordInfo domainToInfo(ProblemSubmitRecordDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemSubmitRecordInfo info = new ProblemSubmitRecordInfo();
        BeanUtils.copyProperties(domain, info);
        info.setProblemInfo(ProblemConvert.domainToInfo(domain.getProblemInfo()));
        info.setStatus(domain.getStatus().value());
        return info;
    }
}
