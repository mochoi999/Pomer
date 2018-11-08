package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.mochoi.pomer.contract.RegisterTaskNavigator;

public class RegisterTaskVM {

    public final ObservableField<Task> task = new ObservableField<>();
    public RegisterTaskNavigator navigator;

    public void setNavigator(RegisterTaskNavigator navigator){
        this.navigator = navigator;
    }

    public void register(){
        Log.d("TEST","%%%%%%%%%%%%%%%%%%%%"+task.get().getTaskName().toString());
    }

    public void backActivity(){
        if(navigator != null){
            navigator.backActivity();
        }
    }

}
