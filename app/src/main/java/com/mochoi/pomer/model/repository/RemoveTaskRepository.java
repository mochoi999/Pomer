package com.mochoi.pomer.model.repository;

/**
 * タスク削除用リポジトリのIF
 */
public interface RemoveTaskRepository {
    void removeTaskById(long id);
}
