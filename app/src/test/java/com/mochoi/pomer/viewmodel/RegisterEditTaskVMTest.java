package com.mochoi.pomer.viewmodel;

import com.mochoi.pomer.di.AppComponent;
import com.mochoi.pomer.di.DaggerAppComponent;
import com.mochoi.pomer.di.TaskRepositoryModule;
import com.mochoi.pomer.infra.FindTaskRepositoryImpl;
import com.mochoi.pomer.infra.RegisterModTaskRepositoryImpl;
import com.mochoi.pomer.infra.RemoveTaskRepositoryImpl;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;
import com.mochoi.pomer.model.repository.RemoveTaskRepository;
import com.mochoi.pomer.model.vo.TaskKind;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

import static org.mockito.Mockito.*;

public class RegisterEditTaskVMTest {
    private RegisterModTaskRepository registerModTaskRepository;
    private FindTaskRepository findTaskRepository;

    @Module
    public class TaskRepositoryModuleMock extends TaskRepositoryModule {
        @Override
        public RegisterModTaskRepository provideRegisterModTaskRepository() {
            registerModTaskRepository = mock(RegisterModTaskRepositoryImpl.class);
            return registerModTaskRepository;
        }
        @Override
        public FindTaskRepository provideFindTaskRepository(){
            findTaskRepository = mock(FindTaskRepositoryImpl.class);
            return findTaskRepository;
        }
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void register() {
        RegisterEditTaskVM vm = DaggerAppComponent
                .builder().taskRepositoryModule(new TaskRepositoryModuleMock()).build().makeRegisterEditTaskVM();
        vm.register(TaskKind.BackLog);
        verify(registerModTaskRepository, times(1)).register(new Task(), "1");
    }

    @Test
    public void getTaskDataById() {
        RegisterEditTaskVM vm = DaggerAppComponent
                .builder().taskRepositoryModule(new TaskRepositoryModuleMock()).build().makeRegisterEditTaskVM();
        vm.getTaskDataById(1L);
        verify(findTaskRepository, times(1)).findById(1L);
    }

//    @Test
//    public void modify() {
//        RegisterEditTaskVM vm = DaggerAppComponent
//                .builder().taskRepositoryModule(new TaskRepositoryModuleMock()).build().makeRegisterEditTaskVM();
//        vm.modify();
//        verify(registerModTaskRepository, times(1)).modify(new Task(), "1");
//    }
}