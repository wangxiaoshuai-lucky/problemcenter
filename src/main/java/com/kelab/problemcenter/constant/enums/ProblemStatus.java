package com.kelab.problemcenter.constant.enums;

public enum ProblemStatus {
    IN_REVIEW(1), PASS_REVIEW(2), REJECT_REVIEW(3);

    private int value;

    ProblemStatus(int value) {
        this.value = value;
    }

    public static ProblemStatus valueOf(int value) {
        switch (value) {
            case 1:return IN_REVIEW;
            case 2:return PASS_REVIEW;
            case 3:return REJECT_REVIEW;
        }
        throw new IllegalArgumentException("ProblemStatus parse wrong");
    }

    public int value() {
        return this.value;
    }
}
