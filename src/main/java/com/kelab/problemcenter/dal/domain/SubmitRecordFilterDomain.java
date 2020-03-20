package com.kelab.problemcenter.dal.domain;

public class SubmitRecordFilterDomain {

    private boolean withProblemInfo;

    private boolean withCreatorInfo;

    public SubmitRecordFilterDomain() {
    }

    public SubmitRecordFilterDomain(boolean withProblemInfo, boolean withCreatorInfo) {
        this.withProblemInfo = withProblemInfo;
        this.withCreatorInfo = withCreatorInfo;
    }

    public boolean isWithProblemInfo() {
        return withProblemInfo;
    }

    public void setWithProblemInfo(boolean withProblemInfo) {
        this.withProblemInfo = withProblemInfo;
    }

    public boolean isWithCreatorInfo() {
        return withCreatorInfo;
    }

    public void setWithCreatorInfo(boolean withCreatorInfo) {
        this.withCreatorInfo = withCreatorInfo;
    }
}
