package com.mochoi.pomer.di;

import com.mochoi.pomer.model.service.CalcTodaysPomoService;
import com.mochoi.pomer.viewmodel.BacklogVM;
import com.mochoi.pomer.viewmodel.PastTaskVM;
import com.mochoi.pomer.viewmodel.RegisterEditTaskVM;
import com.mochoi.pomer.viewmodel.ReportVM;
import com.mochoi.pomer.viewmodel.TimerVM;
import com.mochoi.pomer.viewmodel.TodolistVM;

import dagger.Component;

@Component(modules = TaskRepositoryModule.class)
public interface AppComponent {
    RegisterEditTaskVM makeRegisterEditTaskVM();
    TodolistVM makeTodolistVM();
    BacklogVM makeBacklogVM();
    TimerVM makeTimerVM();
    PastTaskVM makePastTaskVM();
    ReportVM makeReportVM();

    CalcTodaysPomoService makeCalcTodaysPomoService();
}