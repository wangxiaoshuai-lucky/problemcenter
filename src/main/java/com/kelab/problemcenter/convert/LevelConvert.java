package com.kelab.problemcenter.convert;

import com.kelab.info.problemcenter.info.LevelInfo;
import com.kelab.info.problemcenter.info.LevelProblemInfo;
import com.kelab.problemcenter.dal.domain.LevelDomain;
import com.kelab.problemcenter.dal.domain.LevelProblemDomain;
import com.kelab.problemcenter.dal.model.LevelModel;
import com.kelab.problemcenter.dal.model.LevelProblemModel;
import com.kelab.problemcenter.result.level.ProblemResult;
import org.springframework.beans.BeanUtils;

public class LevelConvert {

    public static LevelModel domainToModel(LevelDomain domain) {
        if (domain == null) {
            return null;
        }
        LevelModel model = new LevelModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static LevelDomain modelToDomain(LevelModel model) {
        if (model == null) {
            return null;
        }
        LevelDomain domain = new LevelDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static LevelInfo domainToInfo(LevelDomain domain) {
        if (domain == null) {
            return null;
        }
        LevelInfo info = new LevelInfo();
        BeanUtils.copyProperties(domain, info);
        return info;
    }

    public static LevelDomain infoToDomain(LevelInfo info) {
        if (info == null) {
            return null;
        }
        LevelDomain domain = new LevelDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }

    public static LevelProblemDomain infoToDomain(LevelProblemInfo info) {
        if (info == null) {
            return null;
        }
        LevelProblemDomain domain = new LevelProblemDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }


    public static LevelProblemModel domainToModel(LevelProblemDomain domain) {
        if (domain == null) {
            return null;
        }
        LevelProblemModel model = new LevelProblemModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static LevelProblemDomain modelToDomain(LevelProblemModel model) {
        if (model == null) {
            return null;
        }
        LevelProblemDomain domain = new LevelProblemDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static ProblemResult domainToResult(LevelProblemDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemResult result = new ProblemResult();
        BeanUtils.copyProperties(domain, result);
        return result;
    }
}
