package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.TaskRO;
import com.mochoi.pomer.model.RegisterTaskService;

/**
 * タスク登録用ビューモデル
 */
public class RegisterTaskVM {

    public final ObservableField<Task> task = new ObservableField<>();

    public void register(){
        TaskRO taskRO = new TaskRO();
        taskRO.taskName = task.get().taskName;
        new RegisterTaskService().register(taskRO);

    }

}
