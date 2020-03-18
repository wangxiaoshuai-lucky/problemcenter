package com.kelab.problemcenter.convert;

import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.info.problemcenter.info.ProblemTagsInfo;
import com.kelab.problemcenter.constant.enums.ProblemStatus;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.model.ProblemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProblemConvert {


    public static ProblemModel domainToModel(ProblemDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemModel model = new ProblemModel();
        BeanUtils.copyProperties(domain, model);
        if (domain.getStatus() != null) {
            model.setStatus(domain.getStatus().value());
        }
        return model;
    }

    public static ProblemDomain modelToDomain(ProblemModel model) {
        if (model == null) {
            return null;
        }
        ProblemDomain domain = new ProblemDomain();
        BeanUtils.copyProperties(model, domain);
        domain.setStatus(ProblemStatus.valueOf(model.getStatus()));
        return domain;
    }

    public static ProblemInfo domainToInfo(ProblemDomain domain) {
        if (domain == null) {
            return null;
        }
        ProblemInfo info = new ProblemInfo();
        BeanUtils.copyProperties(domain, info);
        info.setStatus(domain.getStatus().value());
        if (domain.getSubmitInfoDomain() != null) {
            info.setAcNum(domain.getSubmitInfoDomain().getAcNum());
            info.setSubmitNum(domain.getSubmitInfoDomain().getSubmitNum());
        }
        // 转换tagsInfo
        if (CollectionUtils.isEmpty(domain.getTagsDomains())) {
            info.setTagsInfos(Collections.emptyList());
        } else {
            List<ProblemTagsInfo> tagsInfos = new ArrayList<>();
            domain.getTagsDomains().forEach(item -> tagsInfos.add(ProblemTagsConvert.domainToInfo(item)));
            info.setTagsInfos(tagsInfos);
        }
        // 转换ac状态
        if (domain.getUserStatus() != null) {
            info.setUserStatus(domain.getUserStatus().value());
        }
        return info;
    }

}
