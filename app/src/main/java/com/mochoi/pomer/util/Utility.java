package com.mochoi.pomer.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ユーティリティ突っ込みクラス
 */
public class Utility {

    /**
     * 年月日の文字列をDate型に変換。フォーマットはyyyy/MM/dd
     * @param date 年月日の文字列
     * @return Date型の年月日
     */
    public static Date convString2Date(String date) throws ParseException {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        return sdFormat.parse(date);
    }

    /**
     * 処理日（年月日）を取得
     * @return 年月日
     */
    public static Date getNowYear2Date() {
        Calendar calendar = Calendar.getInstance();
        return DateUtils.truncate(calendar.getTime(), Calendar.DATE);
    }
}
