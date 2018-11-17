package com.mochoi.pomer.model.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

/**
 * 稼働ポモドーロ数（実績）管理用オブジェクト
 */
public class WorkedPomo extends RealmObject {
    @PrimaryKey
    public long id;
    public Date registerDate;
    @LinkingObjects("workedPomos")
    public final RealmResults<Task> tasks = null;
}