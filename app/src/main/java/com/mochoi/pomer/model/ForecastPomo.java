package com.mochoi.pomer.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ForecastPomo extends RealmObject {
    @PrimaryKey
    public long id;
    public String pomodoroCount;
    public Date registerDate;
}
