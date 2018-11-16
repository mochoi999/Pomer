package com.mochoi.pomer.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * タスク削除用サービス
 */
public class RemoveTaskService {
    Realm realm;

    public RemoveTaskService(){
        realm = Realm.getDefaultInstance();
    }

    public void removeTaskById(long id){
        realm.beginTransaction();
        RealmResults<Task> results = realm.where(Task.class).equalTo("id", id).findAll();
        results.deleteFirstFromRealm();
        realm.commitTransaction();

    }
}
