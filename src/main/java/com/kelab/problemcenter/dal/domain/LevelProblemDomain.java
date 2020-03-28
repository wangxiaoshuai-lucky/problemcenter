package com.kelab.problemcenter.dal.domain;

import com.kelab.problemcenter.dal.model.LevelProblemModel;

import java.util.Objects;

public class LevelProblemDomain extends LevelProblemModel {

    private Integer id;

    private Integer proId;

    private Integer levelId;

    private Integer grade;

    private String title;

    private boolean ac;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getProId() {
        return proId;
    }

    @Override
    public void setProId(Integer proId) {
        this.proId = proId;
    }

    @Override
    public Integer getLevelId() {
        return levelId;
    }

    @Override
    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    @Override
    public Integer getGrade() {
        return grade;
    }

    @Override
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LevelProblemDomain)) return false;
        LevelProblemDomain domain = (LevelProblemDomain) o;
        return getProId().equals(domain.getProId()) &&
                getLevelId().equals(domain.getLevelId()) &&
                getGrade().equals(domain.getGrade());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProId(), getLevelId(), getGrade());
    }
}