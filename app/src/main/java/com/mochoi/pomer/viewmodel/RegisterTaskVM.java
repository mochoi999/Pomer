package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.mochoi.pomer.contract.RegisterTaskNavigator;

public class RegisterTaskVM {

    public final ObservableField<Task> task = new ObservableField<>();
    public ObservableField<String> aaa = new ObservableField<>();
    public RegisterTaskNavigator navigator;

    public void setNavigator(RegisterTaskNavigator navigator){
        this.navigator = navigator;
    }

    public void register(){
        String taskName = task.get().taskName;

    }

    public void backActivity(){
        if(navigator != null){
            navigator.backActivity();
        }
    }

}
