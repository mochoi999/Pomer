package com.mochoi.pomer.infra;

import com.mochoi.pomer.model.entity.ForecastPomo;
import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.vo.TaskKind;

import java.util.Arrays;
import java.util.Date;
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
        task.isFinished = results.isFinished;
        task.forecastPomos = results.forecastPomos;
        task.workedPomos = results.workedPomos;
        task.reasons = results.reasons;
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

    @Override
    public List<Task> findFinishedList(Date fromDate, Date toDate) {
        RealmResults<Task> results = realm.where(Task.class)
                .equalTo("isFinished", true)
                .between("registerDate", fromDate, toDate)
                .findAll().sort("id", Sort.DESCENDING);
        List<Task> tasks = null;
        if(results != null){
            tasks = Arrays.asList(results.toArray(new Task[0]));
        }
        return tasks;
    }

    @Override
    public List<Reason> findReason(Date fromDate, Date toDate){
        RealmResults<Reason> results = realm.where(Reason.class)
                .between("registerDate", fromDate, toDate)
                .findAll().sort("id", Sort.ASCENDING);
        List<Reason> reasons = null;
        if(results != null){
            reasons = Arrays.asList(results.toArray(new Reason[0]));
        }
        return reasons;
    }
}
