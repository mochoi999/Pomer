package com.mochoi.pomer.model.repository;

import com.mochoi.pomer.model.entity.ForecastPomo;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.vo.TaskKind;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * タスク取得用リポジトリのIF
 */
public interface FindTaskRepository {
    List<Task> findBacklogList();

    Task findById(long id);

    List<Task> findTodoList();

    /**
     * タスクの最新の予想ポモドーロ数を取得
     * @param taskId タスクid
     * @return 最新の予想ポモドーロ数
     */
    String findLastForecastPomo(long taskId);

    String countWorkedPomo(long taskId);

}
