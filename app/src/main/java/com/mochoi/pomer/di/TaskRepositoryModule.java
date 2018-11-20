package com.mochoi.pomer.di;

import com.mochoi.pomer.infra.FindTaskRepository;
import com.mochoi.pomer.infra.RegisterModTaskRepository;
import com.mochoi.pomer.infra.RemoveTaskRepository;
import com.mochoi.pomer.model.repository.FindTaskRepositoryIF;
import com.mochoi.pomer.model.repository.RegisterModTaskRepositoryIF;
import com.mochoi.pomer.model.repository.RemoveTaskRepositoryIF;

import dagger.Module;
import dagger.Provides;

@Module
public class TaskRepositoryModule {
    @Provides
    RegisterModTaskRepositoryIF provideRegisterModTaskRepository() {
        return new RegisterModTaskRepository();
    }
    @Provides
    FindTaskRepositoryIF provideFindTaskRepository(){
        return new FindTaskRepository();
    }
    @Provides
    RemoveTaskRepositoryIF provideRemoveTaskRepository(){
        return new RemoveTaskRepository();
    }
}