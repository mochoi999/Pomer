package com.mochoi.pomer.infra;

import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.RemoveTaskRepository;

import io.realm.Realm;
import io.realm.RealmResults;


public class RemoveTaskRepositoryImpl implements RemoveTaskRepository {
    private Realm realm;

    public RemoveTaskRepositoryImpl(Realm realm){
        this.realm = realm;
    }

    public void removeTaskById(long id){
        realm.beginTransaction();
        RealmResults<Task> results = realm.where(Task.class).equalTo("id", id).findAll();
        results.deleteFirstFromRealm();
        realm.commitTransaction();

    }
}
