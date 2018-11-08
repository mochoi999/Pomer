package com.mochoi.pomer.model;

import android.util.Log;

import com.mochoi.pomer.viewmodel.TaskRO;

import io.realm.Realm;
import io.realm.RealmResults;

public class TaskRegisterService {
    Realm realm;

    public TaskRegisterService(){
        realm = Realm.getDefaultInstance();
    }

    public void register(TaskRO task){

        Number maxid = realm.where(TaskRO.class).max("id");
        if (maxid == null){
            maxid = 0;
        }

        realm.beginTransaction();
        task.id = maxid.longValue() + 1;
        realm.copyToRealm(task);
        realm.commitTransaction();
    }
}
