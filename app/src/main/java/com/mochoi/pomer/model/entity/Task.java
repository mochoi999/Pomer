package com.mochoi.pomer.model.entity;

import com.mochoi.pomer.model.vo.TaskKind;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * タスクオブジェクト
 */
public class Task extends RealmObject {

    @PrimaryKey
    public long id;
    public String taskName = "";
    public int taskKind = TaskKind.BackLog.getValue();
    public boolean isWorking = false;
    public boolean isFinished = false;
    public RealmList<ForecastPomo> forecastPomos = new RealmList<>();
    public RealmList<WorkedPomo> workedPomos = new RealmList<>();
    public RealmList<Reason> reasons = new RealmList<>();
}
