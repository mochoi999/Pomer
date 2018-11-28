package com.mochoi.pomer.model.vo;

/**
 * SharedPreferenceのid
 */
public enum IdSharedPreference {
    SettingPomodoroTime("pomodoroTime")
    ;

    private String value;

    private IdSharedPreference(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
