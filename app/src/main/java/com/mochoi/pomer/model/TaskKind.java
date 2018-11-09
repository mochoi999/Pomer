package com.mochoi.pomer.model;

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
}
