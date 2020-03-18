package com.kelab.problemcenter.convert;

import com.kelab.info.problemcenter.info.ProblemTagsInfo;
import com.kelab.problemcenter.dal.domain.ProblemTagsDomain;
import com.kelab.problemcenter.dal.model.ProblemTagsModel;
import org.springframework.beans.BeanUtils;

public class ProblemTagsConvert {

    public static ProblemTagsModel domainToModel(ProblemTagsDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemTagsModel model = new ProblemTagsModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static ProblemTagsDomain modelToDomain(ProblemTagsModel model) {
        if (model == null) {
            return null;
        }
        ProblemTagsDomain domain = new ProblemTagsDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static ProblemTagsInfo domainToInfo(ProblemTagsDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemTagsInfo info = new ProblemTagsInfo();
        BeanUtils.copyProperties(domain, info);
        return info;
    }

    public static ProblemTagsDomain infoToDomain(ProblemTagsInfo info) {
        if (info == null) {
            return null;
        }
        ProblemTagsDomain domain = new ProblemTagsDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }
}
