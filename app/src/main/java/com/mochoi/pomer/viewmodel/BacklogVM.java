package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.infra.FindTaskRepository;
import com.mochoi.pomer.infra.RegisterModTaskRepository;
import com.mochoi.pomer.infra.RemoveTaskRepository;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.vo.TaskKind;

import java.util.ArrayList;
import java.util.List;

/**
 * バックログ画面用ビューモデル
 */
public class BacklogVM {
    public final ObservableField<List<BacklogItemVM>> items = new ObservableField<>();

    public void refreshTaskList(){
        List<Task> tasks = new FindTaskRepository().findBacklogList();
        List<BacklogItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            BacklogItemVM vm = new BacklogItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);
    }

    public void removeTask(long id){
        new RemoveTaskRepository().removeTaskById(id);
    }

    public void modifyBacklog2Todo(Long[] ids){
        new RegisterModTaskRepository().modifyTaskKind(ids, TaskKind.ToDoToday.getValue());
    }
}
