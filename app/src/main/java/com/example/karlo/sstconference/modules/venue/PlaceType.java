package com.example.karlo.sstconference.modules.venue;

public enum PlaceType {

    RESTAURANT("restaurant"),
    CAFE("cafe"),
    BAR("bar"),

    CHURCH("church"),
    ZOO("zoo"),
    LIBRARY("library"),
    MUSEUM("museum");

    private String value;

    PlaceType(final String value) {
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