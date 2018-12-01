package com.mochoi.pomer.model.entity;

import com.mochoi.pomer.model.entity.Task;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

/**
 * 想定ポモドーロ数（予定）管理オブジェクト
 */
public class ForecastPomo extends RealmObject {
    @PrimaryKey
    public long id;
    public String pomodoroCount;
    public Date registerDate;
    @LinkingObjects("forecastPomos")
    public final RealmResults<Task> task = null;
}
