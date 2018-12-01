package com.mochoi.pomer.model.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Reason extends RealmObject {
    @PrimaryKey
    public long id;
    public int kind;
    public String reason = "";
    public Date registerDate;
    @LinkingObjects("reasons")
    public final RealmResults<Task> task = null;
}
