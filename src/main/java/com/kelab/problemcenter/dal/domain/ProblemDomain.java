package com.kelab.problemcenter.dal.domain;

import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.problemcenter.constant.enums.MarkType;
import com.kelab.problemcenter.constant.enums.ProblemStatus;

import java.util.List;

public class ProblemDomain {

    private Integer id;

    private String title;

    private String description;

    private String input;

    private String output;

    private String sampleInput;

    private String sampleOutput;

    private String hint;

    private Integer timeLimit;

    private Integer memoryLimit;

    private Boolean specialJudge;

    private String specialJudgeSource;

    private String frameSource;

    private Integer frameSourceCompilerId;

    private String source;

    private ProblemStatus status;

    private Integer creatorId;

    private Long createTime;

    private Long modifyTime;

    private Integer acNum;

    private Integer submitNum;

    private MarkType userStatus;

    private Boolean userCollect;

    private List<ProblemTagsDomain> tagsDomains;

    private UserInfo creatorInfo;

    private ProblemSubmitInfoDomain submitInfoDomain;

    public ProblemDomain() {
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getAcNum() {
        return acNum;
    }

    public void setAcNum(Integer acNum) {
        this.acNum = acNum;
    }

    public Integer getSubmitNum() {
        return submitNum;
    }

    public void setSubmitNum(Integer submitNum) {
        this.submitNum = submitNum;
    }

    public ProblemStatus getStatus() {
        return status;
    }

    public void setStatus(ProblemStatus status) {
        this.status = status;
    }

    public MarkType getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(MarkType userStatus) {
        this.userStatus = userStatus;
    }

    public Boolean getUserCollect() {
        return userCollect;
    }

    public void setUserCollect(Boolean userCollect) {
        this.userCollect = userCollect;
    }

    public List<ProblemTagsDomain> getTagsDomains() {
        return tagsDomains;
    }

    public void setTagsDomains(List<ProblemTagsDomain> tagsDomains) {
        this.tagsDomains = tagsDomains;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public UserInfo getCreatorInfo() {
        return creatorInfo;
    }

    public void setCreatorInfo(UserInfo creatorInfo) {
        this.creatorInfo = creatorInfo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getSampleInput() {
        return sampleInput;
    }

    public void setSampleInput(String sampleInput) {
        this.sampleInput = sampleInput;
    }

    public String getSampleOutput() {
        return sampleOutput;
    }

    public void setSampleOutput(String sampleOutput) {
        this.sampleOutput = sampleOutput;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public Boolean getSpecialJudge() {
        return specialJudge;
    }

    public void setSpecialJudge(Boolean specialJudge) {
        this.specialJudge = specialJudge;
    }

    public String getSpecialJudgeSource() {
        return specialJudgeSource;
    }

    public void setSpecialJudgeSource(String specialJudgeSource) {
        this.specialJudgeSource = specialJudgeSource;
    }

    public String getFrameSource() {
        return frameSource;
    }

    public void setFrameSource(String frameSource) {
        this.frameSource = frameSource;
    }

    public Integer getFrameSourceCompilerId() {
        return frameSourceCompilerId;
    }

    public void setFrameSourceCompilerId(Integer frameSourceCompilerId) {
        this.frameSourceCompilerId = frameSourceCompilerId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ProblemSubmitInfoDomain getSubmitInfoDomain() {
        return submitInfoDomain;
    }

    public void setSubmitInfoDomain(ProblemSubmitInfoDomain submitInfoDomain) {
        this.submitInfoDomain = submitInfoDomain;
    }
}