package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ReportVM {
    public final ObservableField<String> fromDate = new ObservableField<>();
    public final ObservableField<String> toDate = new ObservableField<>();
    public final ObservableField<String> avgWorkedPomo = new ObservableField<>();
    public final ObservableField<String> taskCountDiffForcastAndWorked = new ObservableField<>();
    public final ObservableField<String> taskCountDiffDenominator = new ObservableField<>();
    private FindTaskRepository findTaskRepository;

    @Inject
    public ReportVM(FindTaskRepository findTaskRepository){
        this.findTaskRepository = findTaskRepository;
    }

    public List<Reason> findReasonForGraph(Date fromDate, Date toDate){
        return findTaskRepository.findReasonForGraph(fromDate, toDate);
    }

    public void setAvgWorkedPomo4Week(Date fromDate, Date toDate){
        List<WorkedPomo> pomos = findTaskRepository.findWorkedPomoInTerm(fromDate, toDate);

        if(pomos.isEmpty()){
            avgWorkedPomo.set("-");
            return;
        }

        //実績数を、ポモドーロをやった日数の和で割る
        int day = 1;
        Date date = pomos.get(0).registerDate;
        for(WorkedPomo wp : pomos){
            if(date.getTime() != wp.registerDate.getTime()){
                day++;
                date = wp.registerDate;
            }
        }

        int avg = Math.round(pomos.size() / day);
        avgWorkedPomo.set(String.valueOf(avg));
    }

    public void setDiffTaskForecastAndWorked(Date fromDate, Date toDate){
        List<Task> tasks = findTaskRepository.findTaskWorked(fromDate, toDate);
        if(tasks == null){
            taskCountDiffForcastAndWorked.set("-");
            taskCountDiffDenominator.set("-");
            return;
        }

        int count = 0;
        for(Task task : tasks){
            String firstForecastPomo = findTaskRepository.findFirstForecastPomo(task.id);
            String workedPomoCount = findTaskRepository.countWorkedPomo(task.id);
            if(!firstForecastPomo.equals(workedPomoCount)){
                count++;
            }
        }
        taskCountDiffForcastAndWorked.set("" + count);
        taskCountDiffDenominator.set("" + tasks.size());
    }
}
