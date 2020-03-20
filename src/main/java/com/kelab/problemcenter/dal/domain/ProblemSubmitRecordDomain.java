package com.kelab.problemcenter.dal.domain;

import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.problemcenter.constant.enums.ProblemJudgeStatus;

public class ProblemSubmitRecordDomain {

    private Integer id;

    private Integer userId;

    private Integer problemId;

    private Integer compilerId;

    private String source;

    private Integer codeLength;

    private ProblemJudgeStatus status;

    private Integer timeUsed;

    private Integer memoryUsed;

    private String errorMessage;

    private Long submitTime;

    private UserInfo userInfo;

    private ProblemDomain problemInfo;

    public ProblemJudgeStatus getStatus() {
        return status;
    }

    public void setStatus(ProblemJudgeStatus status) {
        this.status = status;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ProblemDomain getProblemInfo() {
        return problemInfo;
    }

    public void setProblemInfo(ProblemDomain problemInfo) {
        this.problemInfo = problemInfo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public Integer getCompilerId() {
        return compilerId;
    }

    public void setCompilerId(Integer compilerId) {
        this.compilerId = compilerId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(Integer codeLength) {
        this.codeLength = codeLength;
    }

    public Integer getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(Integer timeUsed) {
        this.timeUsed = timeUsed;
    }

    public Integer getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Integer memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Long submitTime) {
        this.submitTime = submitTime;
    }
}
