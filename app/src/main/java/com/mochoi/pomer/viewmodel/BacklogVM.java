package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BacklogVM {
    public final ObservableField<List<Task>> tasks = new ObservableField<>();

    public void setUpTaskList(){

        List<Task> list = new ArrayList<>();
        Task t = new Task();
        t.taskName="aaaaa";
        list.add(t);
        list.add(t);
        list.add(t);
        tasks.set(list);
    }
}
