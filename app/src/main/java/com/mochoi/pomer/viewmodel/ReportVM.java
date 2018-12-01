package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ReportVM {
    public final ObservableField<String> fromDate = new ObservableField<>();
    public final ObservableField<String> toDate = new ObservableField<>();
    public final ObservableField<String> avgWorkedPomo = new ObservableField<>();
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
            avgWorkedPomo.set("0");
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
}
