package com.mochoi.pomer.model;

import io.realm.Realm;

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
