package com.mochoi.pomer.infra;

import com.mochoi.pomer.model.entity.ForecastPomo;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.vo.TaskKind;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class FindTaskRepositoryImpl implements FindTaskRepository {
    private Realm realm;

    public FindTaskRepositoryImpl(Realm realm){
        this.realm = realm;
    }

    public List<Task> findBacklogList(){
        RealmResults<Task> results = realm.where(Task.class)
                .equalTo("taskKind", TaskKind.BackLog.getValue())
                .equalTo("isFinished", false)
                .findAll().sort("id", Sort.DESCENDING);
        List<Task> tasks = null;
        if(results != null){
            tasks = Arrays.asList(results.toArray(new Task[0]));
        }
        return tasks;
    }

    public Task findById(long id){
        //FIXME 詰め直ししたくない
        Task results = realm.where(Task.class)
                .equalTo("id", id)
                .findFirst();
        Task task = new Task();
        task.id = id;
        task.taskName = results.taskName;
        task.taskKind = results.taskKind;
        task.isWorking = results.isWorking;
        task.forecastPomos = results.forecastPomos;
        return task;
    }

    public List<Task> findTodoList(){
        RealmResults<Task> results = realm.where(Task.class)
                .equalTo("taskKind", TaskKind.ToDoToday.getValue())
                .equalTo("isFinished", false)
                .findAll().sort("id", Sort.DESCENDING);
        List<Task> tasks = null;
        if(results != null){
            tasks = Arrays.asList(results.toArray(new Task[0]));
        }
        return tasks;
    }

    public String findLastForecastPomo(long taskId){
        RealmResults<ForecastPomo> results = realm.where(ForecastPomo.class)
                .equalTo("tasks.id", taskId)
                .findAll();
        return results.last().pomodoroCount;
    }

    public String countWorkedPomo(long taskId){
        RealmResults<WorkedPomo> results = realm.where(WorkedPomo.class)
                .equalTo("tasks.id", taskId)
                .findAll();
        return String.valueOf(results.size());
    }

}
