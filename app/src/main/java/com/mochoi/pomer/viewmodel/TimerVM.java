package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;

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
    public final ObservableLong time = new ObservableLong();
    public final ObservableLong second = new ObservableLong();
    public final ObservableBoolean isShowReason = new ObservableBoolean(false);
    public final ObservableBoolean isShowFinishStatus = new ObservableBoolean(false);

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

    public void registerReason(ReasonKind reasonKind, String reason){
        registerModTaskRepository.registerReason(task.get().id, reasonKind, reason);
    }

    public void registerWorkedPomo(){
        long taskId = task.get().id;
        registerModTaskRepository.registerWorkedPomo(taskId);
        workedPomo.set(findTaskRepository.countWorkedPomo(taskId));
    }

    public void finishTask(){
        registerModTaskRepository.modifyFinishStatus(task.get().id, true);
    }
}
