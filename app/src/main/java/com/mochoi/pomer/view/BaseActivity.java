package com.mochoi.pomer.view;

import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

/**
 * アクティビティ基底クラス
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * ユーザー向けメッセージ通知処理
     * @param message メッセージ
     */
    public void showNotification(final String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        toast.show();
    }
}
