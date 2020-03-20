package com.kelab.problemcenter.dal.domain;

public class ProblemFilterDomain {

    private boolean withSubmitInfo;

    private boolean withTagsInfo;

    private boolean withCreatorInfo;

    public ProblemFilterDomain() {
    }

    public ProblemFilterDomain(boolean withSubmitInfo, boolean withTagsInfo, boolean withCreatorInfo) {
        this.withSubmitInfo = withSubmitInfo;
        this.withTagsInfo = withTagsInfo;
        this.withCreatorInfo = withCreatorInfo;
    }

    public boolean isWithSubmitInfo() {
        return withSubmitInfo;
    }

    public void setWithSubmitInfo(boolean withSubmitInfo) {
        this.withSubmitInfo = withSubmitInfo;
    }

    public boolean isWithTagsInfo() {
        return withTagsInfo;
    }

    public void setWithTagsInfo(boolean withTagsInfo) {
        this.withTagsInfo = withTagsInfo;
    }

    public boolean isWithCreatorInfo() {
        return withCreatorInfo;
    }

    public void setWithCreatorInfo(boolean withCreatorInfo) {
        this.withCreatorInfo = withCreatorInfo;
    }
}
