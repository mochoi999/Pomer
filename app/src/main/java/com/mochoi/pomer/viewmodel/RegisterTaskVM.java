package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.mochoi.pomer.contract.RegisterTaskNavigator;
import com.mochoi.pomer.model.TaskRegisterService;

public class RegisterTaskVM {

    public final ObservableField<Task> task = new ObservableField<>();
    public RegisterTaskNavigator navigator;

    public void setNavigator(RegisterTaskNavigator navigator){
        this.navigator = navigator;
    }

    public void register(){
        TaskRO taskRO = new TaskRO();
        taskRO.taskName = task.get().taskName;
        new TaskRegisterService().register(taskRO);

    }

    public void backActivity(){
        if(navigator != null){
            navigator.backActivity();
        }
    }

}
