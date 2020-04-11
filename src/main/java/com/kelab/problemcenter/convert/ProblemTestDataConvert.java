package com.kelab.problemcenter.convert;

import com.kelab.info.problemcenter.info.ProblemTestDataInfo;
import com.kelab.problemcenter.dal.domain.ProblemTestDataDomain;
import com.kelab.problemcenter.dal.model.ProblemTestDataModel;
import org.springframework.beans.BeanUtils;

public class ProblemTestDataConvert {

    public static ProblemTestDataDomain infoToDomain(ProblemTestDataInfo info) {
        if (info == null) {
            return null;
        }
        ProblemTestDataDomain domain = new ProblemTestDataDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }

    public static ProblemTestDataInfo domainToInfo(ProblemTestDataDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemTestDataInfo info = new ProblemTestDataInfo();
        BeanUtils.copyProperties(domain, info);
        return info;
    }


    public static ProblemTestDataModel domainToModel(ProblemTestDataDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemTestDataModel model = new ProblemTestDataModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static ProblemTestDataDomain modelToDomain(ProblemTestDataModel model) {
        if (model == null) {
            return null;
        }
        ProblemTestDataDomain domain = new ProblemTestDataDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }
}
