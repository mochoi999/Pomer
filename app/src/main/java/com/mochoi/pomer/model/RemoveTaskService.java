package com.mochoi.pomer.model;

import io.realm.Realm;
import io.realm.RealmResults;

public class RemoveTaskService {
    Realm realm;

    public RemoveTaskService(){
        realm = Realm.getDefaultInstance();
    }

    public void removeTask(long id){
        realm.beginTransaction();
        RealmResults<Task> results = realm.where(Task.class).equalTo("id", id).findAll();
        results.deleteFirstFromRealm();
        realm.commitTransaction();

    }
}
