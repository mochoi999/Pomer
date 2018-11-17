package com.mochoi.pomer.model.vo;

/**
 * 理由の種別
 */
public enum ReasonKind {
    /**
     * 中断
     */
    InComplete(1)
    /**
     * 完了：集中できた
     */
    ,GoodConcentration(2)
    /**
     * 完了：まあまあ集中できた
     */
    ,NomalConcentration(3)
    /**
     * 完了：集中できない
     */
    ,NotConcentrate(4)
    ;

    private int value;

    private ReasonKind(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
