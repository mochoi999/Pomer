package com.mochoi.pomer.view;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
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



/**
 * タイマー画面用アクティビティ
 */
public class TimerActivity extends BaseActivity {
    private TimerVM vm;
    private TimerThread timerThread;
    private RegisterDiffReason diffReasonFragment;
    /**
     * 通知処理用のID
     */
    private int NOTIFICATION_ID = 0;

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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startTimer(View view){

        SharedPreferences pref = getSharedPreferences(NameSharedPreference.Setting.getValue(), Context.MODE_PRIVATE);
        int pomodoroTime = pref.getInt(IdSharedPreference.SettingPomodoroTime.getValue(), 25);
        vm.setTimeValue(pomodoroTime);

        vm.isStarted.set(true);
        vm.modifyStartPomodoro();
        timerThread = new TimerThread();
        timerThread.start();
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

    private class TimerThread extends Thread {
        private boolean running = true;
        public void run() {
            int minutes = vm.time.get() -1;
            for (int i = minutes; i >-1; i--) {
                if(!running){
                    return;
                }
                vm.time.set(i);

                int second = 59;
                vm.second.set(second);
                for (int j = second; j >-1; j--) {
                    if(!running){
                        return;
                    }
                    vm.second.set(j);
                    //通知
                    showNotificationCompat(R.id.timer, i + ":" + j);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            finishPomodoro();
        }

        void stopRunning(){
            this.running = false;
        }
    }

    /**
     * 通知を出す
     */
    private void showNotificationCompat(int viewId, String text){
        String channelId = "pomer";

        //カスタムレイアウト
        RemoteViews customView = new RemoteViews(getPackageName(), R.layout.notification_main);
        //いったんクリアしてからセット
        customView.setTextViewText(R.id.timer, "");
        customView.setTextViewText(viewId, text);

        // 通知をタップしたときにActivityを起動
        Intent intent = new Intent(this, TimerActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0
                , intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.tomato)
                .setAutoCancel(false)   //    通知をタップしたときに、その通知を消すかどうか
                .setOngoing(true)
                .setContent(customView)
                .setContentIntent(contentIntent)
                .setVisibility(Notification.VISIBILITY_PUBLIC)//ロック画面通知の表示の詳細レベル
        .build();
        notification.flags += Notification.FLAG_ONGOING_EVENT;
        notification.flags += Notification.FLAG_NO_CLEAR;


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Notification　Channel 設定
//            NotificationChannel channel = new NotificationChannel(
//                    channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription(message);
//            channel.enableVibration(true);
//            channel.canShowBadge();
//            channel.enableLights(true);
//            channel.setLightColor(Color.BLUE);
//            // the channel appears on the lockscreen
//            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//            channel.setSound(defaultSoundUri, null);
//            channel.setShowBadge(true);
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(NOTIFICATION_ID, notification);
    }

    private void cancelNotificationCompat(){
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.cancel(NOTIFICATION_ID);
    }


    private void finishPomodoro(){
        //play sound
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        SoundPool soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();//stream 同時に扱う効果音の数
        int mp3 = getResources().getIdentifier("alarm", "raw", getPackageName());
        final int soundID = soundPool.load(getBaseContext(), mp3, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundID,1f, 1f, 0, 0, 1);
            }
        });

        vm.isShowFinishStatus.set(true);
        showNotificationCompat(R.id.text,"ポモドーロ時間が終了しました");
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
        timerThread.stopRunning();
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
                reasonKind = ReasonKind.NomalConcentration;
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
