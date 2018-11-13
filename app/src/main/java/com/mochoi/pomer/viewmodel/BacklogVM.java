package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.FindTaskService;
import com.mochoi.pomer.model.RegisterModTaskService;
import com.mochoi.pomer.model.RemoveTaskService;
import com.mochoi.pomer.model.Task;
import com.mochoi.pomer.model.TaskKind;

import java.util.ArrayList;
import java.util.List;

/**
 * バックログ画面用ビューモデル
 */
public class BacklogVM {
    public final ObservableField<List<BacklogItemVM>> items = new ObservableField<>();

    public void refreshTaskList(){
        List<Task> tasks = new FindTaskService().findBacklogList();
        List<BacklogItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            BacklogItemVM vm = new BacklogItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);
    }

    public void removeTask(long id){
        new RemoveTaskService().removeTaskById(id);
    }

    public void modifyBacklog2Todo(Long[] ids){
        new RegisterModTaskService().modifyTaskKind(ids, TaskKind.ToDoToday.getValue());
    }
}
