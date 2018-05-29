package com.example.karlo.sstconference.models.enums;

public enum CommitteeType {

    STEERING("steering"),
    ORGANIZING("organizing"),
    PROGRAM("program");

    private String value;

    CommitteeType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
