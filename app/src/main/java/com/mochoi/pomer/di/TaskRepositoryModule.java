package com.mochoi.pomer.di;

import com.mochoi.pomer.infra.FindTaskRepositoryImpl;
import com.mochoi.pomer.infra.RegisterModTaskRepositoryImpl;
import com.mochoi.pomer.infra.RemoveTaskRepositoryImpl;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;
import com.mochoi.pomer.model.repository.RemoveTaskRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class TaskRepositoryModule {
    @Provides
    RegisterModTaskRepository provideRegisterModTaskRepository() {
        return new RegisterModTaskRepositoryImpl();
    }
    @Provides
    FindTaskRepository provideFindTaskRepository(){
        return new FindTaskRepositoryImpl();
    }
    @Provides
    RemoveTaskRepository provideRemoveTaskRepository(){
        return new RemoveTaskRepositoryImpl();
    }
}