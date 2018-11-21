package com.mochoi.pomer.model.repository;

import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.vo.ReasonKind;
import com.mochoi.pomer.model.vo.TaskKind;

/**
 * タスク登録・更新用リポジトリのIF
 */
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
     * タスクの完了状態を更新
     * @param id 更新対象のid
     * @param status true:完了 false:未完了
     */
    void modifyFinishStatus(long id, boolean status);

    /**
     * タスクに諸作業の状態の理由を登録
     * @param taskId 更新対象のタスクid
     * @param reasonKind 理由種別
     * @param reasonStr 理由
     */
    void registerReason(long taskId, ReasonKind reasonKind, String reasonStr);
}
