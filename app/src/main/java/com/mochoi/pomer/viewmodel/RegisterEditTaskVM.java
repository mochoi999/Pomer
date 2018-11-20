package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;
import com.mochoi.pomer.model.vo.TaskKind;

import javax.inject.Inject;

/**
 * タスク登録・更新画面用ビューモデル
 */
public class RegisterEditTaskVM {
    public final ObservableField<Task> task = new ObservableField<>();
    public final ObservableField<String> forecastPomo = new ObservableField<>();
    public final ObservableBoolean isRegisterMode = new ObservableBoolean(true);
    private RegisterModTaskRepository registerModTaskRepository;
    private FindTaskRepository findTaskRepository;

    @Inject
    public RegisterEditTaskVM(RegisterModTaskRepository registerModTaskRepository
                            ,FindTaskRepository findTaskRepository){
        this.registerModTaskRepository = registerModTaskRepository;
        this.findTaskRepository = findTaskRepository;
    }

    public void register(TaskKind kind){
        task.get().taskKind = kind.getValue();
        registerModTaskRepository.register(task.get(), forecastPomo.get());
    }

    public Task getTaskDataById(long id){
        return findTaskRepository.findById(id);
    }

    public void modifyById(){
        registerModTaskRepository.modify(task.get(), forecastPomo.get());
    }

}
