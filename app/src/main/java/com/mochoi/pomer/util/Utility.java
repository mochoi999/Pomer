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
     * 日付を指定フォーマットで文字列に変換。
     * @param date 日付
     * @param format フォーマット
     * @return 日付文字列
     */
    public static String convDate2String(Date date, String format){
        SimpleDateFormat sdFormat = new SimpleDateFormat(format);
        return sdFormat.format(date);
    }

    /**
     * 処理日（年月日）を取得
     * @return 年月日
     */
    public static Date getNowDate() {
        Calendar calendar = Calendar.getInstance();
        return DateUtils.truncate(calendar.getTime(), Calendar.DATE);
    }

    /**
     * 日付に指定日数を加算
     * @param date 日付
     * @param day 加算日数
     * @return 加算後の日付
     */
    public static Date addDay(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }
}
