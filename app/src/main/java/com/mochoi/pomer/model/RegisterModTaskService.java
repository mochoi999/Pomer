package com.mochoi.pomer.model;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * タスク登録・更新サービス
 */
public class RegisterModTaskService {
    Realm realm;

    public RegisterModTaskService(){
        realm = Realm.getDefaultInstance();
    }

    /**
     * タスクを新規登録
     * @param task タスク
     */
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

    /**
     * idを条件にタスク情報を更新
     * @param task タスク情報
     */
    public void modifyById(Task task){
        realm.beginTransaction();
        Task result = realm.where(Task.class).equalTo("id", task.id).findFirst();
        result.taskName = task.taskName;
        realm.commitTransaction();
    }

    /**
     * バックログタスクをTodoタスクに更新
     * @param ids 更新対象のid配列
     */
    public void modifyBacklog2Todo(Long[] ids){
        realm.beginTransaction();
        RealmResults<Task> results = realm.where(Task.class).in("id", ids).findAll();
        for(Task task : results){
            task.taskKind = TaskKind.ToDoToday.getValue();
        }
        realm.commitTransaction();
    }

    /**
     * Todoタスクをバックログタスクに更新
     * @param id 更新対象のタスクのid
     */
    public void modifyTodo2Backlog(long id){
        realm.beginTransaction();
        Task result = realm.where(Task.class).equalTo("id", id).findFirst();
        result.taskKind = TaskKind.BackLog.getValue();
        realm.commitTransaction();
    }

}
