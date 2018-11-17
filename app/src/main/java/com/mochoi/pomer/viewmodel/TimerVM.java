package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.mochoi.pomer.infra.FindTaskRepository;
import com.mochoi.pomer.infra.RegisterModTaskRepository;
import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.vo.ReasonKind;


/**
 * タイマー画面用ビューモデル
 */
public class TimerVM {
    public final ObservableField<Task> task = new ObservableField<>();
    public final ObservableField<String> forecastPomo = new ObservableField<>();
    public final ObservableField<String> workedPomo = new ObservableField<>();
    public final ObservableBoolean isStarted = new ObservableBoolean(false);
    public int timeInitValue = 25;//TODO 設定画面で設定できるように
    public final ObservableInt time = new ObservableInt(timeInitValue);
    public final ObservableInt second = new ObservableInt();
    public final ObservableBoolean isShowReason = new ObservableBoolean(false);

    public void setUpTaskData(long id){
        FindTaskRepository findTaskService = new FindTaskRepository();
        task.set(findTaskService.findById(id));
        forecastPomo.set(findTaskService.findForecastPomo(id));
        workedPomo.set(findTaskService.countWorkedPomo(id));
    }

    public void modifyStartPomodoro(){
        Task data = task.get();
        data.isWorking = true;
        new RegisterModTaskRepository().modifyById(data, null);
    }

    public void registerReason(String reasonStr){
        Reason reason = new Reason();
        reason.kind = ReasonKind.InComplete.getValue();
        reason.reason = reasonStr;
        new RegisterModTaskRepository().registerReason(task.get().id, reason);
    }

    public void initializeTimeValue(){
        time.set(timeInitValue);
    }

    public void registerWorkedPomo(){
        long taskId = task.get().id;
        new RegisterModTaskRepository().registerWorkedPomo(taskId);
        workedPomo.set(new FindTaskRepository().countWorkedPomo(taskId));
    }
}
