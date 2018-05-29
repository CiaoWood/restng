package com.microdev.automation.restng.keydrive.enums;

public enum Type {

    HTTP("http"), MYSQL("mysql");

    private String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    protected void setValue(String value) {
        this.value = value;
    }

}