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
 * Todoリスト画面用ビューモデル
 */
public class TodolistVM {
    public final ObservableField<List<TodolistItemVM>> items = new ObservableField<>();

    public void refreshTaskList(){
        List<Task> tasks = new FindTaskRepository().findTodoList();
        List<TodolistItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            TodolistItemVM vm = new TodolistItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);
    }

    public void removeTask(long id){
        new RemoveTaskRepository().removeTaskById(id);
    }
    public void modifyTodo2Backlog(Long[] ids){
        new RegisterModTaskRepository().modifyTaskKind(ids, TaskKind.BackLog.getValue());
    }
}
