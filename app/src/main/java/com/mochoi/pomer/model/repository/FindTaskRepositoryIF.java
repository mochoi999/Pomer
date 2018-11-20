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
 * タスク取得サービス
 */
public interface FindTaskRepositoryIF {
    List<Task> findBacklogList();

    Task findById(long id);

    List<Task> findTodoList();

    String findForecastPomo(long taskId);

    String countWorkedPomo(long taskId);

}
