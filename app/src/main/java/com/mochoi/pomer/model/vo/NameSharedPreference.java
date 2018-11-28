package com.mochoi.pomer.model.vo;

/**
 * SharedPreference名
 */
public enum NameSharedPreference {
    Setting("setting")
    ;

    private String value;

    private NameSharedPreference(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
