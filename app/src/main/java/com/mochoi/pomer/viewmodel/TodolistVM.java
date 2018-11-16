package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.mochoi.pomer.model.FindTaskService;
import com.mochoi.pomer.model.RegisterModTaskService;
import com.mochoi.pomer.model.RemoveTaskService;
import com.mochoi.pomer.model.Task;
import com.mochoi.pomer.model.TaskKind;

import java.util.ArrayList;
import java.util.List;

/**
 * Todoリスト画面用ビューモデル
 */
public class TodolistVM {
    public final ObservableField<List<TodolistItemVM>> items = new ObservableField<>();

    public void refreshTaskList(){
        List<Task> tasks = new FindTaskService().findTodoList();
        List<TodolistItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            TodolistItemVM vm = new TodolistItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);
    }

    public void removeTask(long id){
        new RemoveTaskService().removeTaskById(id);
    }
    public void modifyTodo2Backlog(Long[] ids){
        new RegisterModTaskService().modifyTaskKind(ids, TaskKind.BackLog.getValue());
    }
}
