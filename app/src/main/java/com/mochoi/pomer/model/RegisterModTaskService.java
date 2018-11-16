package com.mochoi.pomer.model;

import android.util.Log;

import java.util.Calendar;

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
     *             TODO
     */
    public void register(Task task, String forecastPomo){
        realm.beginTransaction();

        Number maxid = realm.where(Task.class).max("id");
        if (maxid == null){
            maxid = 0;
        }
        task.id = maxid.longValue() + 1;

        //予想ポモドーロ数
        Number maxidFp = realm.where(ForecastPomo.class).max("id");
        if(maxidFp == null){
            maxidFp = 1;
        }
        ForecastPomo fp = new ForecastPomo();
        fp.id = maxidFp.longValue() + 1;
        fp.pomodoroCount = forecastPomo;
        fp.registerDate = Calendar.getInstance().getTime();
        task.forecastPomos.add(fp);

        realm.copyToRealm(task);
        realm.commitTransaction();
    }

    /**
     * idを条件にタスク情報を更新
     * @param task タスク情報
     */
    public void modifyById(Task task, String forecastPomo){
        realm.beginTransaction();
        Task result = realm.where(Task.class).equalTo("id", task.id).findFirst();
        result.taskName = task.taskName;
        result.taskKind = task.taskKind;
        result.isWorking = task.isWorking;
        result.isFinished = task.isFinished;

        if(forecastPomo != null) {
            ForecastPomo fp = new ForecastPomo();
            Number maxidFp = realm.where(ForecastPomo.class).max("id");
            fp.id = maxidFp.longValue() + 1;
            fp.pomodoroCount = forecastPomo;
            fp.registerDate = Calendar.getInstance().getTime();
            result.forecastPomos.add(fp);
        }

        realm.commitTransaction();
    }

    /**
     * タスク種別を更新
     * @param ids 更新対象のid配列
     * @param taskKind タスク種別
     */
    public void modifyTaskKind(Long[] ids, int taskKind){
        realm.beginTransaction();
        RealmResults<Task> results = realm.where(Task.class).in("id", ids).findAll();
        for(Task task : results){
            task.taskKind = taskKind;
        }
        realm.commitTransaction();
    }

    /**
     * タスクの完了状態を更新
     * @param id 更新対象のid
     * @param status true:完了 false:未完了
     */
    public void modifyFinishStatusById(long id, boolean status){
        realm.beginTransaction();
        Task results = realm.where(Task.class).equalTo("id", id).findFirst();
        results.isFinished = status;
        realm.commitTransaction();
    }

    /**
     * 稼働ポモドーロ（実績）を登録
     * @param id 更新対象のタスクid
     */
    public void registerWorkedPomo(long id){
        realm.beginTransaction();
        Task task = realm.where(Task.class).equalTo("id", id).findFirst();

        WorkedPomo wp = new WorkedPomo();
        Number maxid = realm.where(WorkedPomo.class).max("id");
        if (maxid == null){
            maxid = 0;
        }
        wp.id = maxid.longValue() + 1;
        wp.registerDate = Calendar.getInstance().getTime();

        task.workedPomos.add(wp);
        realm.commitTransaction();
    }

}
