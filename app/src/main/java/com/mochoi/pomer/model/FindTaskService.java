package com.mochoi.pomer.model;

import com.mochoi.pomer.viewmodel.Task;

import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * タスク取得サービス
 */
public class FindTaskService {
    Realm realm;

    public FindTaskService(){
        realm = Realm.getDefaultInstance();
    }

    public void findNotFinished(TaskRO task){
//        RealmResults<TaskRO> tasks = realm.where(TaskRO.class).findAll();
//        Arrays.asList(tasks.toArray(Task))
    }
}
