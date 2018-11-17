package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mochoi.pomer.infra.FindTaskRepository;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.infra.RegisterModTaskRepository;
import com.mochoi.pomer.model.vo.TaskKind;

/**
 * タスク登録・更新画面用ビューモデル
 */
public class RegisterEditTaskVM {

    public final ObservableField<Task> task = new ObservableField<>();
    public final ObservableField<String> forecastPomo = new ObservableField<>();
    public final ObservableBoolean isRegisterMode = new ObservableBoolean(true);

    public void register(TaskKind kind){
        task.get().taskKind = kind.getValue();
        new RegisterModTaskRepository().register(task.get(), forecastPomo.get());
    }

    public Task getTaskDataById(long id){
        return new FindTaskRepository().findById(id);
    }

    public void modifyById(){
        new RegisterModTaskRepository().modifyById(task.get(), forecastPomo.get());
    }

}
