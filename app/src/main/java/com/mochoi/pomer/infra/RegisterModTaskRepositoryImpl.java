package com.mochoi.pomer.infra;

import android.support.annotation.NonNull;

import com.mochoi.pomer.model.entity.ForecastPomo;
import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;
import com.mochoi.pomer.model.vo.TaskKind;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class RegisterModTaskRepositoryImpl implements RegisterModTaskRepository {
    private Realm realm;

    public RegisterModTaskRepositoryImpl(Realm realm){
        this.realm = realm;
    }

    @Override
    public Task register(String taskName, TaskKind taskKind, String forecastPomo){
        realm.beginTransaction();

        Task task = new Task();
        task.taskName = taskName;
        task.taskKind = taskKind.getValue();

        Number maxid = realm.where(Task.class).max("id");
        if (maxid == null){
            maxid = 0;
        }
        task.id = maxid.longValue() + 1L;

        //予想ポモドーロ数
        Number maxidFp = realm.where(ForecastPomo.class).max("id");
        if(maxidFp == null){
            maxidFp = 1;
        }
        ForecastPomo fp = new ForecastPomo();
        fp.id = maxidFp.longValue() + 1L;
        fp.pomodoroCount = forecastPomo;
        fp.registerDate = Calendar.getInstance().getTime();
        task.forecastPomos.add(fp);

        realm.copyToRealm(task);
        realm.commitTransaction();

        return task;
    }

    @Override
    public void modify(Task task, String forecastPomo){
        realm.beginTransaction();
        Task result = realm.where(Task.class).equalTo("id", task.id).findFirst();
        result.taskName = task.taskName;
        result.taskKind = task.taskKind;
        result.isWorking = task.isWorking;
        result.isFinished = task.isFinished;

        if(forecastPomo != null
                && !forecastPomo.equals(result.forecastPomos.last().pomodoroCount)) {
            ForecastPomo fp = new ForecastPomo();
            Number maxidFp = realm.where(ForecastPomo.class).max("id");
            fp.id = maxidFp.longValue() + 1L;
            fp.pomodoroCount = forecastPomo;
            fp.registerDate = Calendar.getInstance().getTime();
            result.forecastPomos.add(fp);
        }

        realm.commitTransaction();
    }

    @Override
    public void modifyTaskKind(Long[] ids, int taskKind){
        realm.beginTransaction();
        RealmResults<Task> results = realm.where(Task.class).in("id", ids).findAll();
        for(Task task : results){
            task.taskKind = taskKind;
        }
        realm.commitTransaction();
    }

//    /**
//     * タスクの完了状態を更新
//     * @param id 更新対象のid
//     * @param status true:完了 false:未完了
//     */
//    public void modifyFinishStatusById(long id, boolean status){
//        realm.beginTransaction();
//        Task results = realm.where(Task.class).equalTo("id", id).findFirst();
//        results.isFinished = status;
//        realm.commitTransaction();
//    }

    @Override
    public void registerWorkedPomo(long taskId){
        realm.beginTransaction();
        WorkedPomo wp = new WorkedPomo();
        Number maxid = realm.where(WorkedPomo.class).max("id");
        if (maxid == null){
            maxid = 0;
        }
        wp.id = maxid.longValue() + 1;
        wp.registerDate = Calendar.getInstance().getTime();//FIXME

        Task task = realm.where(Task.class).equalTo("id", taskId).findFirst();
        task.workedPomos.add(wp);
        realm.commitTransaction();
    }


    @Override
    public void registerReason(long taskId, Reason reason){
        realm.beginTransaction();
        Number maxid = realm.where(Reason.class).max("id");
        if (maxid == null){
            maxid = 0;
        }
        reason.id = maxid.longValue() + 1;
        reason.registerDate = Calendar.getInstance().getTime();

        Task task = realm.where(Task.class).equalTo("id", taskId).findFirst();
        task.reasons.add(reason);
        realm.commitTransaction();
    }

}
