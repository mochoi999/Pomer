package com.mochoi.pomer.model;

import android.util.Log;

import io.realm.Realm;

/**
 * タスク登録サービス
 */
public class RegisterEditTaskService {
    Realm realm;

    public RegisterEditTaskService(){
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

    public void modifyById(Task task){
        realm.beginTransaction();
        Task result = realm.where(Task.class).equalTo("id", task.id).findFirst();
        result.taskName = task.taskName;
        realm.commitTransaction();
    }
}
