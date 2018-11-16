package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.mochoi.pomer.model.FindTaskService;
import com.mochoi.pomer.model.NotFinishedReason;
import com.mochoi.pomer.model.NotFinishedReasonService;
import com.mochoi.pomer.model.RegisterModTaskService;
import com.mochoi.pomer.model.Task;


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
        FindTaskService findTaskService = new FindTaskService();
        task.set(findTaskService.findById(id));
        forecastPomo.set(findTaskService.findForecastPomo(id));
        workedPomo.set(findTaskService.countWorkedPomo(id));
    }

    public void modifyStartPomodoro(){
        Task data = task.get();
        data.isWorking = true;
        new RegisterModTaskService().modifyById(data, null);
    }

    public void registerReason(String reasonStr){
        NotFinishedReason reason = new NotFinishedReason();
        reason.taskId = task.get().id;
        reason.reason = reasonStr;
        new NotFinishedReasonService().register(reason);
    }

    public void initializeTimeValue(){
        time.set(timeInitValue);
    }

    public void registerWorkedPomo(){
        long taskId = task.get().id;
        new RegisterModTaskService().registerWorkedPomo(taskId);
        workedPomo.set(new FindTaskService().countWorkedPomo(taskId));
    }
}
