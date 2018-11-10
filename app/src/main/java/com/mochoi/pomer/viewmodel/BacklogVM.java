package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.FindTaskService;
import com.mochoi.pomer.model.RegisterModTaskService;
import com.mochoi.pomer.model.RemoveTaskService;
import com.mochoi.pomer.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BacklogVM {
    public final ObservableField<List<BacklogItemVM>> items = new ObservableField<>();

    public void setUpTaskList(){
        List<Task> tasks = new FindTaskService().findNotFinished();
        List<BacklogItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            BacklogItemVM vm = new BacklogItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);
    }

    public void removeTask(long id){
        new RemoveTaskService().removeTask(id);
    }

    public void modifyBacklog2Todo(Long[] ids){
        new RegisterModTaskService().modifyBacklog2Todo(ids);
    }
}
