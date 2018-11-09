package com.mochoi.pomer.model;

import android.util.Log;

import io.realm.Realm;

/**
 * タスク登録サービス
 */
public class RegisterTaskService {
    Realm realm;

    public RegisterTaskService(){
        realm = Realm.getDefaultInstance();
    }

    public void register(Task task){
        realm.beginTransaction();

        Number maxid = realm.where(Task.class).max("id");
        if (maxid == null){
            maxid = 0;
        }

        task.id = maxid.longValue() + 1;
        realm.copyToRealm(task);
        realm.commitTransaction();
    }
}
