package com.kelab.problemcenter.convert;

import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.model.ProblemModel;
import org.springframework.beans.BeanUtils;

public class ProblemConvert {

    public static ProblemDomain modelToDomain(ProblemModel model) {
        if (model == null) {
            return null;
        }
        ProblemDomain domain = new ProblemDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

}
