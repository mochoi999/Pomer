package com.mochoi.pomer.model;

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
    public String forecastPomo = "";
}
