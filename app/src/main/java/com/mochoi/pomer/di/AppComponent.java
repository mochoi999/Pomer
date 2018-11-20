package com.mochoi.pomer.di;

import com.mochoi.pomer.viewmodel.RegisterEditTaskVM;
import com.mochoi.pomer.viewmodel.TodolistVM;

import dagger.Component;

@Component(modules = TaskRepositoryModule.class)
public interface AppComponent {
    RegisterEditTaskVM makeRegisterEditTaskVM();
    TodolistVM makeTodolistVM();
}