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
        when(mockRealm.where(Task.class).max("id")).thenReturn(null);
        when(mockRealm.where(ForecastPomo.class).max("id")).thenReturn(1);

        RegisterModTaskRepositoryImpl repository = new RegisterModTaskRepositoryImpl(mockRealm);
        Task task = new Task();
        task.taskName = "taskname";
        task.taskKind = TaskKind.BackLog.getValue();
        String forecastPomo = "2";
        repository.register(task, forecastPomo);

        assertThat(task.id, is(1L));
        assertThat(task.taskName, is("taskname"));
        assertThat(task.taskKind, is(TaskKind.BackLog.getValue()));
        assertThat(task.isFinished, is(false));
        assertThat(task.isWorking, is(false));
        assertThat(task.forecastPomos.size(), is(1));
        assertThat(task.forecastPomos.get(0).id, is(2L));
        assertThat(task.forecastPomos.get(0).pomodoroCount, is("2"));
        assertThat(task.reasons.size(), is(0));
        assertThat(task.workedPomos.size(), is(0));
        verify(mockRealm, times(1)).copyToRealm(task);
        verify(mockRealm, times(1)).beginTransaction();
        verify(mockRealm, times(1)).commitTransaction();

    }

    @Test
    public void modify_異なる予想ポモ数() {
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
        repository.modify(t2, "3");

        assertThat(t.id, is(t2.id));
        assertThat(t.taskName, is(t2.taskName));
        assertThat(t.taskKind, is(t2.taskKind));
        assertThat(t.isFinished, is(t2.isFinished));
        assertThat(t.isWorking, is(t2.isWorking));
        assertThat(t.forecastPomos.size(),is(2));
        assertThat(t.forecastPomos.last().id,is(2L));
        assertThat(t.forecastPomos.get(1).pomodoroCount,is("3"));
        assertThat(t.workedPomos.size(),is(1));
        assertThat(t.reasons.size(),is(0));
        verify(mockRealm, times(1)).beginTransaction();
        verify(mockRealm, times(1)).commitTransaction();
    }

    @Test
    public void modify_前回と同じ予想ポモ数() {
        Task t = new Task();
        ForecastPomo fp = new ForecastPomo();
        fp.id = 1L;
        fp.pomodoroCount = "1";
        t.forecastPomos.add(fp);

        RegisterModTaskRepository repository = new RegisterModTaskRepositoryImpl(mockRealm);
        Task t2 = new Task();

        when(mockRealm.where(Task.class).equalTo("id", t.id).findFirst()).thenReturn(t);
        when(mockRealm.where(ForecastPomo.class).max("id")).thenReturn(t.forecastPomos.get(0).id);
        repository.modify(t2, "1");

        assertThat(t.forecastPomos.size(), is(1));
        assertThat(t.forecastPomos.get(0).id, is(1L));
        assertThat(t.forecastPomos.get(0).pomodoroCount, is("1"));
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