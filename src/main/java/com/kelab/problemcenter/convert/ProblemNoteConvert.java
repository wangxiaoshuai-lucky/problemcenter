package com.kelab.problemcenter.convert;

import com.kelab.info.problemcenter.info.ProblemNoteInfo;
import com.kelab.problemcenter.dal.domain.ProblemNoteDomain;
import com.kelab.problemcenter.dal.model.ProblemNoteModel;
import org.springframework.beans.BeanUtils;

public class ProblemNoteConvert {

    public static ProblemNoteModel domainToModel(ProblemNoteDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemNoteModel model = new ProblemNoteModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static ProblemNoteDomain modelToDomain(ProblemNoteModel model) {
        if (model == null) {
            return null;
        }
        ProblemNoteDomain domain = new ProblemNoteDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static ProblemNoteInfo domainToInfo(ProblemNoteDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemNoteInfo info = new ProblemNoteInfo();
        BeanUtils.copyProperties(domain, info);
        return info;
    }

    public static ProblemNoteDomain infoToDomain(ProblemNoteInfo info) {
        if (info == null) {
            return null;
        }
        ProblemNoteDomain domain = new ProblemNoteDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }
}
