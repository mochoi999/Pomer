package com.mochoi.pomer.model.repository;

import com.mochoi.pomer.model.entity.Task;

public interface RegisterModTaskRepository {

    /**
     * タスク情報を新規登録
     * @param task タスク
     * @param forecastPomo 予想ポモドーロ数
     */
    void register(Task task, String forecastPomo);
    /**
     * タスク情報を更新
     * @param task タスク情報
     * @param forecastPomo 予想ポモドーロ数
     */
    void modify(Task task, String forecastPomo);
    void modifyTaskKind(Long[] ids, int taskKind);
}
