package com.mochoi.pomer.infra;

import com.mochoi.pomer.model.entity.ForecastPomo;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.repository.RegisterModTaskRepository;
import com.mochoi.pomer.model.vo.TaskKind;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.hamcrest.core.Is.is;

import io.realm.Realm;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Realm.class)
public class RegisterModTaskRepositoryImplTest {

    Realm mockRealm;

    @Before
    public void setUp() throws Exception {
        mockRealm = PowerMockito.mock(Realm.class, RETURNS_DEEP_STUBS);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void register() {
        when(mockRealm.where(Task.class).max("id")).thenReturn(0);
        when(mockRealm.where(ForecastPomo.class).max("id")).thenReturn(0);

        RegisterModTaskRepositoryImpl repository = new RegisterModTaskRepositoryImpl(mockRealm);
        Task task = new Task();
        task.taskName = "taskname";
        task.taskKind = TaskKind.BackLog.getValue();
        String forecastPomo = "2";
        repository.register(task, forecastPomo);

        assertThat(1L, is(task.id));
        assertThat("taskname", is(task.taskName));
        assertThat(TaskKind.BackLog.getValue(), is(task.taskKind));
        assertThat(false, is(task.isFinished));
        assertThat(false, is(task.isWorking));
        assertThat(1, is(task.forecastPomos.size()));
        assertThat(1L, is(task.forecastPomos.get(0).id));
        assertThat("2", is(task.forecastPomos.get(0).pomodoroCount));
        assertThat(0, is(task.reasons.size()));
        assertThat(0, is(task.workedPomos.size()));
        verify(mockRealm, times(1)).copyToRealm(task);
        verify(mockRealm, times(1)).beginTransaction();
        verify(mockRealm, times(1)).commitTransaction();

    }

    @Test
    public void modifyById_異なる予想ポモ数() {
        Task t = new Task();
        t.id = 1L;
        t.taskName = "taskname";
        t.taskKind = TaskKind.BackLog.getValue();
        t.isFinished = false;
        t.isWorking = true;
        ForecastPomo fp = new ForecastPomo();
        fp.id = 1L;
        fp.pomodoroCount = "1";
        t.forecastPomos.add(fp);
        WorkedPomo wp = new WorkedPomo();
        wp.id = 1L;
        t.workedPomos.add(wp);

        RegisterModTaskRepository repository = new RegisterModTaskRepositoryImpl(mockRealm);
        Task t2 = new Task();
        t2.id = 1L;
        t2.taskName = "taskname2";
        t2.taskKind = TaskKind.ToDoToday.getValue();
        t2.isFinished = true;
        t2.isWorking = false;

        when(mockRealm.where(Task.class).equalTo("id", t.id).findFirst()).thenReturn(t);
        when(mockRealm.where(ForecastPomo.class).max("id")).thenReturn(t.forecastPomos.get(0).id);
        Task result = repository.modifyById(t2, "3");

        assertThat(t2.id, is(result.id));
        assertThat(t2.taskName, is(result.taskName));
        assertThat(t2.taskKind, is(result.taskKind));
        assertThat(t2.isFinished, is(result.isFinished));
        assertThat(t2.isWorking, is(result.isWorking));
        assertThat(2, is(result.forecastPomos.size()));
        assertThat(2L, is(result.forecastPomos.last().id));
        assertThat("3", is(result.forecastPomos.get(1).pomodoroCount));
        assertThat(1, is(result.workedPomos.size()));
        assertThat(0, is(result.reasons.size()));
        verify(mockRealm, times(1)).beginTransaction();
        verify(mockRealm, times(1)).commitTransaction();
    }

    @Test
    public void modifyById_前回と同じ予想ポモ数() {
        Task t = new Task();
        ForecastPomo fp = new ForecastPomo();
        fp.id = 1L;
        fp.pomodoroCount = "1";
        t.forecastPomos.add(fp);

        RegisterModTaskRepository repository = new RegisterModTaskRepositoryImpl(mockRealm);
        Task t2 = new Task();

        when(mockRealm.where(Task.class).equalTo("id", t.id).findFirst()).thenReturn(t);
        when(mockRealm.where(ForecastPomo.class).max("id")).thenReturn(t.forecastPomos.get(0).id);
        Task result = repository.modifyById(t2, "1");

        assertThat(1, is(result.forecastPomos.size()));
        assertThat(1L, is(result.forecastPomos.get(0).id));
        assertThat("1", is(result.forecastPomos.get(0).pomodoroCount));
        verify(mockRealm, times(1)).beginTransaction();
        verify(mockRealm, times(1)).commitTransaction();
    }


    @Test
    public void modifyTaskKind() {
    }

    @Test
    public void modifyFinishStatusById() {
    }

    @Test
    public void registerWorkedPomo() {
    }

    @Test
    public void registerReason() {
    }
}