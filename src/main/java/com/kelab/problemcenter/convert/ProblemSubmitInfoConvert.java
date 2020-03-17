package com.kelab.problemcenter.convert;

import com.kelab.problemcenter.dal.domain.ProblemSubmitInfoDomain;
import com.kelab.problemcenter.dal.model.ProblemSubmitInfoModel;
import org.springframework.beans.BeanUtils;

public class ProblemSubmitInfoConvert {

    public static ProblemSubmitInfoDomain modelToDomain(ProblemSubmitInfoModel model) {
        if (model == null) {
            return null;
        }
        ProblemSubmitInfoDomain domain = new ProblemSubmitInfoDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static ProblemSubmitInfoModel domainToModel(ProblemSubmitInfoDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemSubmitInfoModel model = new ProblemSubmitInfoModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

}
