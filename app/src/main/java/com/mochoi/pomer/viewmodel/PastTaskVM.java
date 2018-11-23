package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class PastTaskVM {
    public final ObservableField<List<PastItemVM>> items = new ObservableField<>();
    private FindTaskRepository findTaskRepository;
    private RegisterModTaskRepository registerModTaskRepository;

    @Inject
    public PastTaskVM(FindTaskRepository findTaskRepository
                        ,RegisterModTaskRepository registerModTaskRepository){
        this.findTaskRepository = findTaskRepository;
        this.registerModTaskRepository = registerModTaskRepository;
    }

    public void setUpList(Date fromDate, Date toDate){
        List<Task> tasks = findTaskRepository.findFinishedList(fromDate, toDate);
        List<PastItemVM> items = new ArrayList<>();
        for (Task t : tasks){
            PastItemVM vm = new PastItemVM();
            vm.task.set(t);
            items.add(vm);
        }
        this.items.set(items);
    }

    public void releaseFinishStatus(long taskId){
        registerModTaskRepository.modifyFinishStatus(taskId, false);
    }
}
