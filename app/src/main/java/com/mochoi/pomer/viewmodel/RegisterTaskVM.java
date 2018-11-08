package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.TaskRegisterService;

public class RegisterTaskVM {

    public final ObservableField<Task> task = new ObservableField<>();

    public void register(){
        TaskRO taskRO = new TaskRO();
        taskRO.taskName = task.get().taskName;
        new TaskRegisterService().register(taskRO);

    }

}
