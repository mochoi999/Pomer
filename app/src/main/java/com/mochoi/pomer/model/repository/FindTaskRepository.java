package com.mochoi.pomer.model.repository;

import com.mochoi.pomer.model.entity.ForecastPomo;
import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.vo.TaskKind;

import java.util.Arrays;
import java.util.Date;
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
     * タスクの最初の予想ポモドーロ数を取得
     * @param taskId タスクid
     * @return 最初の予想ポモドーロ数
     */
    String findFirstForecastPomo(long taskId);

    /**
     * タスクの最新の予想ポモドーロ数を取得
     * @param taskId タスクid
     * @return 最新の予想ポモドーロ数
     */
    String findLastForecastPomo(long taskId);

    /**
     * タスクの実績ポモドーロ数を取得
     * @param taskId タスクid
     * @return 実績ポモドーロ数
     */
    String countWorkedPomo(long taskId);

    /**
     * 完了したタスクのリストを取得。指定期間でタスクの登録日を検索。
     * @param fromDate 登録日From
     * @param toDate 登録日To
     * @return タスクリスト
     */
    List<Task> findFinishedList(Date fromDate, Date toDate);

    /**
     * Reportグラフに表示する指定期間内の理由データを取得
     * @param fromDate 期間From
     * @param toDate 期間To
     * @return 理由リスト
     */
    List<Reason> findReasonForGraph(Date fromDate, Date toDate);

    /**
     * Report画面の理由リストに表示する指定期間内の理由データを取得
     * @param fromDate 期間From
     * @param toDate 期間To
     * @return 理由リスト
     */
    List<Reason> findReasonForReportList(Date fromDate, Date toDate);

    /**
     * 指定期間内の実績ポモドーロを取得
     * @param fromDate 期間From
     * @param toDate 期間To
     * @return 実績ポモドーロリスト
     */
    List<WorkedPomo> findWorkedPomoInTerm(Date fromDate, Date toDate);

    /**
     * 指定期間に実績のあるタスクを取得
     * @param fromDate 期間From
     * @param toDate 期間To
     * @return タスクリスト
     */
    List<Task> findTaskWorked(Date fromDate, Date toDate);

}
