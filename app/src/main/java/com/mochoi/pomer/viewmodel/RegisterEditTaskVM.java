package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mochoi.pomer.model.FindTaskService;
import com.mochoi.pomer.model.Task;
import com.mochoi.pomer.model.RegisterModTaskService;

/**
 * タスク登録・更新画面用ビューモデル
 */
public class RegisterEditTaskVM {

    public final ObservableField<Task> task = new ObservableField<>();
    public final ObservableBoolean isRegisterMode = new ObservableBoolean(true);

    public void register(){
        new RegisterModTaskService().register(task.get());
    }

    public Task getTaskDataById(long id){
        return new FindTaskService().findById(id);
    }

    public void modifyById(){
        new RegisterModTaskService().modifyById(task.get());
    }

}
