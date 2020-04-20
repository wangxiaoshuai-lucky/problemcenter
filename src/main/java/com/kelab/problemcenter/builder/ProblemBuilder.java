package com.kelab.problemcenter.builder;

import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.problemcenter.constant.enums.ProblemStatus;
import com.kelab.problemcenter.convert.ProblemTagsConvert;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.dal.domain.ProblemSubmitInfoDomain;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

public class ProblemBuilder {

    public static ProblemDomain buildNewProblemDomain(ProblemInfo info) {
        ProblemDomain domain = buildBasicInfo(info);
        // 新题目 id 为null
        domain.setId(null);
        fillTagsInfos(domain, info);
        // 初始化提交情况
        ProblemSubmitInfoDomain submitInfoDomain = new ProblemSubmitInfoDomain();
        submitInfoDomain.setSubmitNum(0);
        submitInfoDomain.setAcNum(0);
        domain.setSubmitInfoDomain(submitInfoDomain);
        return domain;
    }

    public static ProblemDomain buildUpdateProblemDomain(ProblemInfo info) {
        ProblemDomain domain = buildBasicInfo(info);
        fillTagsInfos(domain, info);
        return domain;
    }

    /**
     * 构建题目的基本信息
     */
    private static ProblemDomain buildBasicInfo(ProblemInfo info) {
        ProblemDomain domain = new ProblemDomain();
        BeanUtils.copyProperties(info, domain);
        if (info.getStatus() != null) {
            domain.setStatus(ProblemStatus.valueOf(info.getStatus()));
        }
        return domain;
    }

    /**
     * 构建标签信息
     */
    private static void fillTagsInfos(ProblemDomain domain, ProblemInfo info) {
        if (!CollectionUtils.isEmpty(info.getTagsInfos())) {
            domain.setTagsDomains(info.getTagsInfos().stream().map(ProblemTagsConvert::infoToDomain).collect(Collectors.toList()));
        }
    }
}
