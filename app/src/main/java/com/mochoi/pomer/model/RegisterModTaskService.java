package com.mochoi.pomer.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * タスク登録サービス
 */
public class RegisterModTaskService {
    Realm realm;

    public RegisterModTaskService(){
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

    public void modifyBacklog2Todo(Long[] ids){
        realm.beginTransaction();
        Log.d("TEST", ""+ids.toString());
        for(Long id : ids){
            Log.d("TEST", ""+id);
        }
        RealmResults<Task> results = realm.where(Task.class).in("id", ids).findAll();
        for(Task task : results){
            task.taskKind = TaskKind.ToDoToday.getValue();
            Log.d("TEST", ""+task.taskKind);
        }
        realm.commitTransaction();
    }
}
