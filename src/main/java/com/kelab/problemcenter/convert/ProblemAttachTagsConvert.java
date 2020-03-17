package com.kelab.problemcenter.convert;

import com.kelab.problemcenter.dal.domain.ProblemAttachTagsDomain;
import com.kelab.problemcenter.dal.model.ProblemAttachTagsModel;
import org.springframework.beans.BeanUtils;

public class ProblemAttachTagsConvert {

    public static ProblemAttachTagsDomain modelToDomain(ProblemAttachTagsModel model) {
        if (model == null) {
            return null;
        }
        ProblemAttachTagsDomain domain = new ProblemAttachTagsDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static ProblemAttachTagsModel domainToModel(ProblemAttachTagsDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemAttachTagsModel model = new ProblemAttachTagsModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }
}
