package com.mochoi.pomer.model.service;

import android.util.Log;

import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.repository.FindTaskRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * 「今日やる予定のポモドーロ数」の計算クラス
 */
public class CalcTodaysPomoService {
    private FindTaskRepository findTaskRepository;
    /**
     * ポモドーロ数算出不可
     */
    public static int CAN_NOT_CALCLATE = -1;

    @Inject
    public CalcTodaysPomoService(FindTaskRepository findTaskRepository){
        this.findTaskRepository = findTaskRepository;
    }


    /**
     * 今日やる予定のポモドーロ数を算出する。<br/>
     * 計算式：未完了のTODOタスクの予想ポモドーロ数 - 未完了のTODOタスクの実績ポモドーロ数<br/>
     * ただし１タスクでも予想ポモ数＞実績ポモ数のタスクがある場合は計算不可とし、{@link CAN_NOT_CALCLATE} を返却する。
     * @return 今日やる予定のポモドーロ数　または　{@link CAN_NOT_CALCLATE}
     */
    public int calc(){
        List<Task> tasks = findTaskRepository.findTodoList();
        int todaysPomodoro = 0;
        for (Task task : tasks){
            int restPomo = task.getLastForecastPomoCount() - task.getWorkedPomoCount();
            if(restPomo < 0){
                return CAN_NOT_CALCLATE;
            } else {
                todaysPomodoro += restPomo;
            }
        }

        return todaysPomodoro;
    }

}
