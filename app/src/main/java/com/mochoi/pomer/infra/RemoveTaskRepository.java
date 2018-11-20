package com.mochoi.pomer.infra;

import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.RemoveTaskRepositoryIF;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * タスク削除用サービス
 */
public class RemoveTaskRepository implements RemoveTaskRepositoryIF {
    Realm realm;

    public RemoveTaskRepository(){
        realm = Realm.getDefaultInstance();
    }

    public void removeTaskById(long id){
        realm.beginTransaction();
        RealmResults<Task> results = realm.where(Task.class).equalTo("id", id).findAll();
        results.deleteFirstFromRealm();
        realm.commitTransaction();

    }
}
