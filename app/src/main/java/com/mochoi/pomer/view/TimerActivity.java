package com.mochoi.pomer.view;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.TimerMainBinding;
import com.mochoi.pomer.di.DaggerAppComponent;
import com.mochoi.pomer.model.vo.IdSharedPreference;
import com.mochoi.pomer.model.vo.NameSharedPreference;
import com.mochoi.pomer.model.vo.ReasonKind;
import com.mochoi.pomer.viewmodel.TimerVM;

import org.apache.commons.lang3.StringUtils;


/**
 * タイマー画面用アクティビティ
 */
public class TimerActivity extends BaseActivity {
    private TimerVM vm;
    private RegisterDiffReason diffReasonFragment;
    private CountDownTimer timer;
    /**
     * 通知処理用のID
     */
    private int NOTIFICATION_ID = 0;
    /**
     * 通知チャンネル用のID タイマー残り時間
     */
    private String CHANNEL_ID_COUNT = "pomer_count";
    /**
     * 通知チャンネル用のID タイマー終了
     */
    private String CHANNEL_ID_FINISH = "pomer_finish";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getIntent().getLongExtra("id", 0);

        TimerMainBinding binding = DataBindingUtil.setContentView(this, R.layout.timer_main);
        vm = DaggerAppComponent.create().makeTimerVM();
        vm.setUpTaskData(id);
        binding.setTimerVM(vm);

        findViewById(R.id.stop_reason).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // ソフトキーボードを非表示にする
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        findViewById(R.id.status_reason).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // ソフトキーボードを非表示にする
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        // アクションバーをカスタマイズ
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Pomodoro");

        //通知チャンネルの設定
        setNotificationCountTimerChannel();
        setNotificationFinishChannel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                if (!vm.isStarted.get() && !vm.isShowFinishStatus.get()) {
                    finish();
                    return true;
                } else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("ポモドーロは終了していません")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                    return false;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void startTimer(View view){
        SharedPreferences pref = getSharedPreferences(NameSharedPreference.Setting.getValue(), Context.MODE_PRIVATE);
        int pomodoroTime = pref.getInt(IdSharedPreference.SettingPomodoroTime.getValue(), 25);

        vm.isStarted.set(true);
        vm.modifyStartPomodoro();

        timer = new MyCountDownTimer(pomodoroTime * 60 * 1000, 1000);
        timer.start();

        //play sound
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        SoundPool soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();//stream 同時に扱う効果音の数
        int mp3 = getResources().getIdentifier("timer", "raw", getPackageName());
        final int soundID = soundPool.load(getBaseContext(), mp3, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundID,1f, 1f, 0, 0, 1);
            }
        });

    }

    private class MyCountDownTimer extends CountDownTimer {
        MyCountDownTimer(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long minute = millisUntilFinished / 1000L / 60L;
            long second = millisUntilFinished / 1000L % 60L;
            vm.time.set(minute);
            vm.second.set(second);

            showNotificationCompat(CHANNEL_ID_COUNT, R.id.timer, minute + ":" + StringUtils.leftPad(String.valueOf(second), 2, "0"),
                    false, true,false);
        }

        @Override
        public void onFinish() {
            vm.time.set(0L);
            vm.second.set(0L);
            finishPomodoro();
        }
    }

    private void setNotificationCountTimerChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_COUNT, "タイマー", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("残り時間の通知");
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void setNotificationFinishChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_FINISH, "カウントダウン終了", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("カウントダウン終了通知");
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setSound(Uri.parse("android.resource://"+getPackageName()+"/raw/alarm"), null);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }

    private void showNotificationCompat(String channelId, int viewId, String text, boolean isAutoCancel,
                                        boolean isGoing, boolean isVibration){
        //カスタムレイアウト
        RemoteViews customView = new RemoteViews(getPackageName(), R.layout.notification_main);
        //いったんクリアしてからセット
        customView.setTextViewText(R.id.timer, "");
        customView.setTextViewText(R.id.text, "");
        customView.setTextViewText(viewId, text);

        // 通知をタップしたときにActivityを起動
        Intent intent = new Intent(this, TimerActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0
                , intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.drawable.tomato);
        builder.setAutoCancel(isAutoCancel);   //    通知をタップしたときに、その通知を消すかどうか
        builder.setOngoing(isGoing);
        builder.setContent(customView);
        builder.setContentIntent(contentIntent);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);//ロック画面通知の表示の詳細レベル

        if(isVibration){
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    private void cancelNotificationCompat(){
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.cancel(NOTIFICATION_ID);
    }


    private void finishPomodoro(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            //Oreo未満の場合、終了通知用のチャンネルが無いためここで鳴らす
            //play sound
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            SoundPool soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();//stream 同時に扱う効果音の数
            int mp3 = getResources().getIdentifier("alarm", "raw", getPackageName());
            final int soundID = soundPool.load(getBaseContext(), mp3, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    soundPool.play(soundID, 1f, 1f, 0, 0, 1);
                }
            });
        }

        vm.isShowFinishStatus.set(true);
        showNotificationCompat(CHANNEL_ID_FINISH, R.id.text,"ポモドーロ時間が終了しました", true, false,true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK
                || (!vm.isStarted.get() && !vm.isShowFinishStatus.get())) {
            return super.onKeyDown(keyCode, event);
        } else {
            //戻るボタンは使えない
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("ポモドーロは終了していません")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
            return false;
        }
    }

    public void finishTaskAlert(View view){
        int forecastPomo = Integer.parseInt(vm.forecastPomo.get());
        int workedPomo = Integer.parseInt(vm.workedPomo.get());
        int diff = forecastPomo - workedPomo;
        if(0 == workedPomo) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("ポモドーロを一度もしていません。完了してよろしいですか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishTask();
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();

        } else if(diff > 3 || diff < -3){
            showDiffReasonFragment();

        } else {
            finishTask();
        }
    }

    public void finishTask(){
        vm.finishTask();
        showNotification("タスクを完了にしました");
        finish();
    }

    ////////// 中断理由登録フラグメント //////////
    public void stopPomodoro(View view){
        vm.isShowReason.set(true);
    }

    public void hideNotFinishReasonView(View view){
        vm.isShowReason.set(false);
    }

    public void registerNotFinishReason(View view){
        String reason = ((TextView)findViewById(R.id.stop_reason)).getText().toString();
        vm.registerReason(ReasonKind.InComplete, reason);

        ((TextView)findViewById(R.id.stop_reason)).setText("");
        timer.cancel();
        vm.isShowReason.set(false);
        vm.isStarted.set(false);
        showNotification("登録しました");

        //通知を消す
        cancelNotificationCompat();
    }

    ////////// 予実差の理由登録フラグメント //////////
    private void showDiffReasonFragment(){
        // フラグメントを生成
        diffReasonFragment = new RegisterDiffReason();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.diff_reason_container, diffReasonFragment, "fragment")
                .commit();
    }

    public void registerDiffReason(View view){
        String reason = ((EditText)findViewById(R.id.diff_reason)).getText().toString();
        vm.registerReason(ReasonKind.DiffActualAndForecast, reason);

        hideDiffReasonFragment();
        finishTask();
    }

    public void hideDiffReasonFragment(View view){
        hideDiffReasonFragment();
    }

    private void hideDiffReasonFragment(){
        //フラグメントを破棄
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .remove(diffReasonFragment)
                .commit();
    }

    ////////// 終了状態登録フラグメント //////////
    public void registerFinishStatus(View view){
        int id = ((RadioGroup)findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        ReasonKind reasonKind = null;
        switch(id){
            case R.id.good :
                reasonKind = ReasonKind.GoodConcentration;
                break;
            case R.id.nomal :
                reasonKind = ReasonKind.NormalConcentration;
                break;
            case R.id.not :
                reasonKind = ReasonKind.NotConcentrate;
                break;
            default:break;
        }

        String reason = ((TextView)findViewById(R.id.status_reason)).getText().toString();
        vm.registerReason(reasonKind, reason);
        vm.registerWorkedPomo();

        ((TextView)findViewById(R.id.status_reason)).setText("");
        ((RadioButton)findViewById(R.id.good)).setChecked(true);

        vm.isStarted.set(false);
        vm.isShowFinishStatus.set(false);
        showNotification("登録しました");
    }

}
