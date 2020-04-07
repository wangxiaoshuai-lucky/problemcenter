package com.kelab.problemcenter.dal.model;

public class ProblemUseMarkModel {

    private Integer id;

    private Integer userId;

    private Integer problemId;

    private Integer markType;

    private Long markTime;

    private Integer submitId;

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

    public Integer getMarkType() {
        return markType;
    }

    public void setMarkType(Integer markType) {
        this.markType = markType;
    }

    public Long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Long markTime) {
        this.markTime = markTime;
    }

    public Integer getSubmitId() {
        return submitId;
    }

    public void setSubmitId(Integer submitId) {
        this.submitId = submitId;
    }
}
