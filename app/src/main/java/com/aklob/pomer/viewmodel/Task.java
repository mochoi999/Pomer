package com.aklob.pomer.viewmodel;

import android.databinding.ObservableField;

public class Task {

    private final ObservableField<String> taskName = new ObservableField<>();

    public Task(){
    }

    public Task(String taskName){
        this.taskName.set(taskName);
    }

    public ObservableField<String> getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName){
        this.taskName.set(taskName);
    }
}
