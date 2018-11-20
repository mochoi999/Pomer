package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.mochoi.pomer.infra.FindTaskRepositoryImpl;
import com.mochoi.pomer.infra.RegisterModTaskRepositoryImpl;
import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.vo.ReasonKind;

import io.realm.Realm;


/**
 * タイマー画面用ビューモデル
 */
public class TimerVM {
    public final ObservableField<Task> task = new ObservableField<>();
    public final ObservableField<String> forecastPomo = new ObservableField<>();
    public final ObservableField<String> workedPomo = new ObservableField<>();
    public final ObservableBoolean isStarted = new ObservableBoolean(false);
    public int timeInitValue = 1;//TODO 設定画面で設定できるように
    public final ObservableInt time = new ObservableInt(timeInitValue);
    public final ObservableInt second = new ObservableInt();
    public final ObservableBoolean isShowReason = new ObservableBoolean(false);

    public void setUpTaskData(long id){
        FindTaskRepositoryImpl findTaskService = new FindTaskRepositoryImpl();
        task.set(findTaskService.findById(id));
        forecastPomo.set(findTaskService.findForecastPomo(id));
        workedPomo.set(findTaskService.countWorkedPomo(id));
    }

    public void modifyStartPomodoro(){
        Task data = task.get();
        data.isWorking = true;
        new RegisterModTaskRepositoryImpl(Realm.getDefaultInstance()).modifyById(data, null);
    }

    public void registerReason(String reasonStr){
        Reason reason = new Reason();
        reason.kind = ReasonKind.InComplete.getValue();
        reason.reason = reasonStr;
        new RegisterModTaskRepositoryImpl(Realm.getDefaultInstance()).registerReason(task.get().id, reason);
    }

    public void initializeTimeValue(){
        time.set(timeInitValue);
    }

    public void registerWorkedPomo(){
        long taskId = task.get().id;
        new RegisterModTaskRepositoryImpl(Realm.getDefaultInstance()).registerWorkedPomo(taskId);
        workedPomo.set(new FindTaskRepositoryImpl().countWorkedPomo(taskId));
    }
}
