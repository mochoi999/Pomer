package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.repository.FindTaskRepository;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ReportVM {
    public final ObservableField<String> fromDate = new ObservableField<>();
    public final ObservableField<String> toDate = new ObservableField<>();
    private FindTaskRepository findTaskRepository;

    @Inject
    public ReportVM(FindTaskRepository findTaskRepository){
        this.findTaskRepository = findTaskRepository;
    }

    public List<Reason> findReason(Date fromDate, Date toDate){
        return findTaskRepository.findReason(fromDate, toDate);
    }
}
