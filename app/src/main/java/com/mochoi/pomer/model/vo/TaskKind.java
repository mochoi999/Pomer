package com.mochoi.pomer.model.vo;

import javax.inject.Inject;

/**
 * タスク種別
 */
public enum TaskKind {
    BackLog(1)
    ,ToDoToday(2)
    ,Interrupt(3);

    private int value;

    private TaskKind(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static TaskKind getKind(int value){
        switch (value){
            case 1: return BackLog;
            case 2: return ToDoToday;
            case 3: return Interrupt;
            default: return null;
        }
    }
}
