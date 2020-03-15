package com.kelab.problemcenter.constant.enums;

public enum MarkType {

    AC(1), CHALLENGING(2), COLLECT(3);

    private int value;

    MarkType(int value) {
        this.value = value;
    }

    public static MarkType valueOf(int value) {
        switch (value) {
            case 1:
                return AC;
            case 2:
                return CHALLENGING;
            case 3:
                return COLLECT;
        }
        throw new RuntimeException("MarkType parse wrong");
    }

    public int value() {
        return this.value;
    }
}
