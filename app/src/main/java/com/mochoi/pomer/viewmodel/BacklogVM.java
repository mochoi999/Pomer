package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.infra.FindTaskRepositoryImpl;
import com.mochoi.pomer.infra.RegisterModTaskRepositoryImpl;
import com.mochoi.pomer.infra.RemoveTaskRepositoryImpl;
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
        List<Task> tasks = new FindTaskRepositoryImpl().findBacklogList();
        List<BacklogItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            BacklogItemVM vm = new BacklogItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);
    }

    public void removeTask(long id){
        new RemoveTaskRepositoryImpl().removeTaskById(id);
    }

    public void modifyBacklog2Todo(Long[] ids){
        new RegisterModTaskRepositoryImpl().modifyTaskKind(ids, TaskKind.ToDoToday.getValue());
    }
}
