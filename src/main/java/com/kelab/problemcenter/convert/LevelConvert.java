package com.kelab.problemcenter.convert;

import com.kelab.info.problemcenter.info.LevelInfo;
import com.kelab.problemcenter.dal.domain.LevelDomain;
import com.kelab.problemcenter.dal.model.LevelModel;
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
}
