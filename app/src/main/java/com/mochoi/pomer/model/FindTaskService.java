package com.mochoi.pomer.model;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * タスク取得サービス
 */
public class FindTaskService {
    Realm realm;

    public FindTaskService(){
        realm = Realm.getDefaultInstance();
    }

    public List<Task> findNotFinished(){
        RealmResults<Task> results = realm.where(Task.class)
                .equalTo("taskKind", TaskKind.BackLog.getValue())
                .equalTo("isFinished", false)
                .findAll().sort("id", Sort.DESCENDING);
        List<Task> tasks = null;
        if(results != null){
            tasks = Arrays.asList(results.toArray(new Task[0]));
        }
        return tasks;
    }
}
