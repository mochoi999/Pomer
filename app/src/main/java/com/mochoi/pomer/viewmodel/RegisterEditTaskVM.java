package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.mochoi.pomer.model.FindTaskService;
import com.mochoi.pomer.model.Task;
import com.mochoi.pomer.model.RegisterEditTaskService;

/**
 * タスク登録・更新画面用ビューモデル
 */
public class RegisterEditTaskVM {

    public final ObservableField<Task> task = new ObservableField<>();
    public final ObservableBoolean isRegisterMode = new ObservableBoolean(true);

    public void register(){
        new RegisterEditTaskService().register(task.get());
    }

    public Task getTaskDataById(long id){
        return new FindTaskService().findById(id);
    }

    public void modifyById(){
        new RegisterEditTaskService().modifyById(task.get());
    }

}
