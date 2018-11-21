package com.mochoi.pomer.model.entity;

import com.mochoi.pomer.model.vo.TaskKind;

import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * タスクオブジェクト
 */
public class Task extends RealmObject {

    @PrimaryKey
    public long id;
    public String taskName = "";
    public int taskKind = TaskKind.BackLog.getValue();
    public boolean isWorking = false;
    public boolean isFinished = false;
    public RealmList<ForecastPomo> forecastPomos = new RealmList<>();
    public RealmList<WorkedPomo> workedPomos = new RealmList<>();
    public RealmList<Reason> reasons = new RealmList<>();

    /**
     * 最新の予想ポモドーロ数を取得
     * @return 最新の予想ポモドーロ数
     */
    public int getLastForecastPomoCount(){
        return Integer.parseInt(forecastPomos.last().pomodoroCount);
    }

    /**
     * 実績ポモドーロ数を取得
     * @return 実績ポモドーロ数
     */
    public int getWorkedPomoCount(){
        return workedPomos.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                taskKind == task.taskKind &&
                isWorking == task.isWorking &&
                isFinished == task.isFinished &&
                Objects.equals(taskName, task.taskName) &&
                Objects.equals(forecastPomos, task.forecastPomos) &&
                Objects.equals(workedPomos, task.workedPomos) &&
                Objects.equals(reasons, task.reasons);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, taskName, taskKind, isWorking, isFinished, forecastPomos, workedPomos, reasons);
    }
}
