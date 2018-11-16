package com.mochoi.pomer.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NotFinishedReason extends RealmObject {
    @PrimaryKey
    public long id;
    public long taskId;
    public int kind;
    public String reason = "";

}
