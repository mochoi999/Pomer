package com.mochoi.pomer.infra;

import com.mochoi.pomer.model.entity.ForecastPomo;
import com.mochoi.pomer.model.entity.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import io.realm.Realm;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest(Realm.class)
public class RegisterModTaskRepositoryImplTest {

    Realm mockRealm;

    @Before
    public void setUp() throws Exception {
//        mockStatic(Realm.class);
        mockRealm = PowerMockito.mock(Realm.class, RETURNS_DEEP_STUBS);
//        when(Realm.getDefaultInstance()).thenReturn(mockRealm);
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
        String forecastPomo = "2";
        repository.register(task, forecastPomo);

        verify(mockRealm, times(1)).copyToRealm(task);

    }

    @Test
    public void modifyById() {
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