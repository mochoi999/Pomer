package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;
import com.mochoi.pomer.model.vo.ReasonKind;

import javax.inject.Inject;


/**
 * タイマー画面用ビューモデル
 */
public class TimerVM {
    private FindTaskRepository findTaskRepository;
    private RegisterModTaskRepository registerModTaskRepository;
    public final ObservableField<Task> task = new ObservableField<>();
    public final ObservableField<String> forecastPomo = new ObservableField<>();
    public final ObservableField<String> workedPomo = new ObservableField<>();
    public final ObservableBoolean isStarted = new ObservableBoolean(false);
    private int timeInitValue = 1;//TODO 設定画面で設定できるように
    public final ObservableInt time = new ObservableInt(timeInitValue);
    public final ObservableInt second = new ObservableInt();
    public final ObservableBoolean isShowReason = new ObservableBoolean(false);

    @Inject
    public TimerVM(FindTaskRepository findTaskRepository, RegisterModTaskRepository registerModTaskRepository){
        this.findTaskRepository = findTaskRepository;
        this.registerModTaskRepository = registerModTaskRepository;
    }

    public void setUpTaskData(long id){
        task.set(findTaskRepository.findById(id));
        forecastPomo.set(findTaskRepository.findLastForecastPomo(id));
        workedPomo.set(findTaskRepository.countWorkedPomo(id));
    }

    public void modifyStartPomodoro(){
        Task data = task.get();
        data.isWorking = true;
        registerModTaskRepository.modify(data, null);
    }

    public void registerReason(String reasonStr){
        Reason reason = new Reason();
        reason.kind = ReasonKind.InComplete.getValue();
        reason.reason = reasonStr;
        registerModTaskRepository.registerReason(task.get().id, reason);
    }

    public void initializeTimeValue(){
        time.set(timeInitValue);
    }

    public void registerWorkedPomo(){
        long taskId = task.get().id;
        registerModTaskRepository.registerWorkedPomo(taskId);
        workedPomo.set(findTaskRepository.countWorkedPomo(taskId));
    }
}
