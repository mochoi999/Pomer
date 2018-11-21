package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.infra.FindTaskRepositoryImpl;
import com.mochoi.pomer.infra.RegisterModTaskRepositoryImpl;
import com.mochoi.pomer.infra.RemoveTaskRepositoryImpl;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;
import com.mochoi.pomer.model.repository.RemoveTaskRepository;
import com.mochoi.pomer.model.vo.TaskKind;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * バックログ画面用ビューモデル
 */
public class BacklogVM {
    public final ObservableField<List<BacklogItemVM>> items = new ObservableField<>();
    private FindTaskRepository findTaskRepository;
    private RemoveTaskRepository removeTaskRepository;
    private RegisterModTaskRepository registerModTaskRepository;

    @Inject
    public BacklogVM(FindTaskRepository findTaskRepository
                    ,RemoveTaskRepository removeTaskRepository
                    ,RegisterModTaskRepository registerModTaskRepository){
        this.findTaskRepository = findTaskRepository;
        this.removeTaskRepository = removeTaskRepository;
        this.registerModTaskRepository = registerModTaskRepository;
    }

    public void refreshTaskList(){
        List<Task> tasks = findTaskRepository.findBacklogList();
        List<BacklogItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            BacklogItemVM vm = new BacklogItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);
    }

    public void removeTask(long id){
        removeTaskRepository.removeTaskById(id);
    }

    public void modifyBacklog2Todo(Long[] ids){
        registerModTaskRepository.modifyTaskKind(ids, TaskKind.ToDoToday.getValue());
    }
}
