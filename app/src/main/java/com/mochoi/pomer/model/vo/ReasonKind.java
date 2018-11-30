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
    ,NormalConcentration(3)
    /**
     * 完了：集中できない
     */
    ,NotConcentrate(4)
    /**
     * 完了：予実が異なる
     */
    ,DiffActualAndForecast(5)
    ;

    private int value;

    private ReasonKind(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static ReasonKind getReason(int value){
        switch (value){
            case 1: return InComplete;
            case 2: return GoodConcentration;
            case 3: return NormalConcentration;
            case 4: return NotConcentrate;
            case 5: return DiffActualAndForecast;
            default: return null;
        }
    }

}
