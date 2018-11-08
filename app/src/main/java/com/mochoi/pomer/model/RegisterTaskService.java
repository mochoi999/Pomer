package com.mochoi.pomer.model;

import io.realm.Realm;

public class RegisterTaskService {
    Realm realm;

    public RegisterTaskService(){
        realm = Realm.getDefaultInstance();
    }

    public void register(TaskRO task){
        realm.beginTransaction();

        Number maxid = realm.where(TaskRO.class).max("id");
        if (maxid == null){
            maxid = 0;
        }

        task.id = maxid.longValue() + 1;
        realm.copyToRealm(task);
        realm.commitTransaction();
    }
}
