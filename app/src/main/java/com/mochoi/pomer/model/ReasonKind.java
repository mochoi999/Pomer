package com.mochoi.pomer.model;

/**
 * 未完了理由の種別
 */
public enum ReasonKind {
    /**
     * 中断
     */
    Stop(1)

    /**
     * 未完了
     */
    ,InComplete(2);

    private int value;

    private ReasonKind(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
