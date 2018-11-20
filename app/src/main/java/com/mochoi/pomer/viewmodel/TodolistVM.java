package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.infra.FindTaskRepository;
import com.mochoi.pomer.infra.RegisterModTaskRepository;
import com.mochoi.pomer.infra.RemoveTaskRepository;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.FindTaskRepositoryIF;
import com.mochoi.pomer.model.repository.RegisterModTaskRepositoryIF;
import com.mochoi.pomer.model.repository.RemoveTaskRepositoryIF;
import com.mochoi.pomer.model.vo.TaskKind;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Todoリスト画面用ビューモデル
 */
public class TodolistVM {
    public final ObservableField<List<TodolistItemVM>> items = new ObservableField<>();
    @Inject
    FindTaskRepositoryIF findTaskRepository;
    @Inject
    RegisterModTaskRepositoryIF registerModTaskRepository;
    @Inject
    RemoveTaskRepositoryIF removeTaskRepository;

    @Inject
    public TodolistVM(){}

    public void refreshTaskList(){
        List<Task> tasks = findTaskRepository.findTodoList();
        List<TodolistItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            TodolistItemVM vm = new TodolistItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);
    }

    public void removeTask(long id){
        removeTaskRepository.removeTaskById(id);
    }
    public void modifyTodo2Backlog(Long[] ids){
        registerModTaskRepository.modifyTaskKind(ids, TaskKind.BackLog.getValue());
    }
}
