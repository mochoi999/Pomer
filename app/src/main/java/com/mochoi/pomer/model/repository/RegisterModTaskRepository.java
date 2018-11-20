package com.mochoi.pomer.model.repository;

import com.mochoi.pomer.model.entity.Task;

public interface RegisterModTaskRepository {

    void register(Task task, String forecastPomo);
    void modifyById(Task task, String forecastPomo);
    void modifyTaskKind(Long[] ids, int taskKind);
}
