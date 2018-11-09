package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.FindTaskService;
import com.mochoi.pomer.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BacklogVM {
    public final ObservableField<List<Task>> tasks = new ObservableField<>();

    public void setUpTaskList(){
        tasks.set(new FindTaskService().findNotFinished());
    }
}
