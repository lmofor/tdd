package com.test.tdd.domain.enums;

public enum EOperation {
    DEPOSIT, WITHDRAWAL;

    EOperation() {
    }

    @Override
    public String toString() {
        return "EOperation{}";
    }
}
