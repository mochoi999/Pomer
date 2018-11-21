package com.mochoi.pomer.viewmodel;

import com.mochoi.pomer.di.DaggerAppComponent;
import com.mochoi.pomer.di.TaskRepositoryModule;
import com.mochoi.pomer.infra.FindTaskRepositoryImpl;
import com.mochoi.pomer.infra.RegisterModTaskRepositoryImpl;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;
import com.mochoi.pomer.model.vo.TaskKind;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dagger.Module;

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
        verify(registerModTaskRepository, times(1)).register("name", TaskKind.ToDoToday, "1");
    }

    @Test
    public void getTaskDataById() {
        RegisterEditTaskVM vm = DaggerAppComponent
                .builder().taskRepositoryModule(new TaskRepositoryModuleMock()).build().makeRegisterEditTaskVM();
        vm.getTaskDataById(1L);
        verify(findTaskRepository, times(1)).findById(1L);
    }

    @Test
    public void modify() {
        RegisterEditTaskVM vm = DaggerAppComponent
                .builder().taskRepositoryModule(new TaskRepositoryModuleMock()).build().makeRegisterEditTaskVM();
        Task task = new Task();
        vm.task.set(task);
        vm.forecastPomo.set("1");
        vm.modify();
        verify(registerModTaskRepository, times(1)).modify(task, "1");
    }
}