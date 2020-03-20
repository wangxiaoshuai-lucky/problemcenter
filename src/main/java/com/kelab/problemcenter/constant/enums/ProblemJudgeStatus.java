package com.kelab.problemcenter.constant.enums;

public enum ProblemJudgeStatus {

    WAITING(-1, "Waiting"),
    AC(0, "Accepted"),
    PE(1, "Presentation_Error"),
    TLE(2, "Time_Limit_Exceeded"),
    MLE(3, "Memory_Limit_Exceeded"),
    WA(4, "Wrong_Answer"),
    RE(5, "Runtime_Error"),
    OLE(6, "Output_Limit_Exceeded"),
    CE(7, "Compile_Error"),
    SE(8, "System_Error"),
    SF(9, "Security_Failed");

    private int value;

    private String desc;

    ProblemJudgeStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ProblemJudgeStatus valueOf(int value) {
        switch (value) {
            case -1:
                return WAITING;
            case 0:
                return AC;
            case 1:
                return PE;
            case 2:
                return TLE;
            case 3:
                return MLE;
            case 4:
                return WA;
            case 5:
                return RE;
            case 6:
                return OLE;
            case 7:
                return CE;
            case 8:
                return SE;
            case 9:
                return SF;
        }
        throw new IllegalArgumentException("ProblemJudgeStatus parse wrong");
    }

    public int value() {
        return this.value;
    }
}
