package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.mochoi.pomer.model.FindTaskService;
import com.mochoi.pomer.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * タイマー画面用ビューモデル
 */
public class TimerVM {
    public final ObservableField<Task> task = new ObservableField<>();
    public final ObservableBoolean isStarted = new ObservableBoolean(false);
    public final ObservableInt time = new ObservableInt(25);

    public void setUpTaskData(long id){
        task.set(new FindTaskService().findById(id));
    }

}
