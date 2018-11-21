package com.mochoi.pomer.model.repository;

import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.vo.TaskKind;

public interface RegisterModTaskRepository {

    /**
     * タスク情報を新規登録
     * @param taskName タスク名
     * @param taskKind タスク種別
     * @param forecastPomo 予想ポモドーロ数
     */
    Task register(String taskName, TaskKind taskKind, String forecastPomo);

    /**
     * タスク情報を更新
     * @param task タスク情報
     * @param forecastPomo 予想ポモドーロ数
     */
    void modify(Task task, String forecastPomo);

    /**
     * タスク種別を更新
     * @param ids 更新対象のid配列
     * @param taskKind タスク種別
     */
    void modifyTaskKind(Long[] ids, int taskKind);

    /**
     * 実績ポモドーロを登録
     * @param taskId 更新対象のタスクid
     */
    void registerWorkedPomo(long taskId);

    /**
     * タスクに諸作業の状態の理由を登録
     * @param taskId 更新対象のタスクid
     * @param reason 理由オブジェクト
     */
    void registerReason(long taskId, Reason reason);
}
