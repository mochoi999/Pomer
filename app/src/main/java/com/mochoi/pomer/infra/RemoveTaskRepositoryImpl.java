package com.mochoi.pomer.infra;

import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.RemoveTaskRepository;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * タスク削除用サービス
 */
public class RemoveTaskRepositoryImpl implements RemoveTaskRepository {
    Realm realm;

    public RemoveTaskRepositoryImpl(){
        realm = Realm.getDefaultInstance();
    }

    public void removeTaskById(long id){
        realm.beginTransaction();
        RealmResults<Task> results = realm.where(Task.class).equalTo("id", id).findAll();
        results.deleteFirstFromRealm();
        realm.commitTransaction();

    }
}
