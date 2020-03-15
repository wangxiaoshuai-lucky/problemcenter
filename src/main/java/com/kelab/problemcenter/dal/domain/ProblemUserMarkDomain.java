package com.kelab.problemcenter.dal.domain;

import com.kelab.problemcenter.constant.enums.MarkType;

public class ProblemUserMarkDomain {

    private Integer id;

    private Integer userId;

    private Integer problemId;

    private String title;

    private MarkType markType;

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

    public MarkType getMarkType() {
        return markType;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
