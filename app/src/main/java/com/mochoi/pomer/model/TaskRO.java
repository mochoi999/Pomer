package com.mochoi.pomer.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TaskRO extends RealmObject {
    @PrimaryKey
    public long id;
    public String taskName;

}
