package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.di.DaggerAppComponent;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;
import com.mochoi.pomer.model.repository.RemoveTaskRepository;
import com.mochoi.pomer.model.service.CalcTodaysPomoService;
import com.mochoi.pomer.model.vo.TaskKind;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Todoリスト画面用ビューモデル
 */
public class TodolistVM {
    public final ObservableField<List<TodolistItemVM>> items = new ObservableField<>();
    public final ObservableField<String> todaysPomodoro = new ObservableField<>("0");
    public final ObservableField<String> todaysPomodoroAlert = new ObservableField<>();
    private FindTaskRepository findTaskRepository;
    private RegisterModTaskRepository registerModTaskRepository;
    private RemoveTaskRepository removeTaskRepository;

    @Inject
    public TodolistVM(FindTaskRepository findTaskRepository
            ,RegisterModTaskRepository registerModTaskRepository
            ,RemoveTaskRepository removeTaskRepository) {
        this.findTaskRepository=findTaskRepository;
        this.registerModTaskRepository=registerModTaskRepository;
        this.removeTaskRepository=removeTaskRepository;
    }

    public void refreshTaskList(){
        //list
        List<Task> tasks = findTaskRepository.findTodoList();
        List<TodolistItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            TodolistItemVM vm = new TodolistItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);

        //予定ポモドーロ数
        int pomodoro = calcTodaysPomodoro();
        if(pomodoro == CalcTodaysPomoService.CAN_NOT_CALCLATE){
            todaysPomodoroAlert.set("今日の予定ポモドーロ数が不明です。実績が予想を超えているタスクのポモドーロ数を調整してください");
            todaysPomodoro.set("");
        } else {
            todaysPomodoroAlert.set("");
            todaysPomodoro.set(String.valueOf(pomodoro));
        }
    }

    /**
     * 今日のポモドーロ数を算出する
     */
    private int calcTodaysPomodoro(){
        CalcTodaysPomoService calcTodaysPomoService = DaggerAppComponent.create().makeCalcTodaysPomoService();
        return calcTodaysPomoService.calc();
    }

    public void removeTask(long id){
        removeTaskRepository.removeTaskById(id);
    }
    public void modifyTodo2Backlog(Long[] ids){
        registerModTaskRepository.modifyTaskKind(ids, TaskKind.BackLog.getValue());
    }
}
