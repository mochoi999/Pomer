package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.Task;
import com.mochoi.pomer.model.RegisterTaskService;

/**
 * タスク登録用ビューモデル
 */
public class RegisterTaskVM {

    public final ObservableField<Task> task = new ObservableField<>();

    public void register(){
        Task data = new Task();
        data.taskName = task.get().taskName;
        new RegisterTaskService().register(data);

    }

}
