package com.mochoi.pomer.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.TimerMainBinding;
import com.mochoi.pomer.viewmodel.TimerVM;


/**
 * タイマー画面用アクティビティ
 */
public class TimerActivity extends BaseActivity {
    private TimerVM vm;
    private TimerThread timerThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getIntent().getLongExtra("id", 0);

        TimerMainBinding binding = DataBindingUtil.setContentView(this, R.layout.timer_main);
        vm = new TimerVM();
        vm.setUpTaskData(id);
        binding.setTimerVM(vm);

        findViewById(R.id.reason).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    // ソフトキーボードを非表示にする
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    public void startTimer(View view){
        vm.initializeTimeValue();
        vm.isStarted.set(true);
        vm.modifyStartPomodoro();
        timerThread = new TimerThread();
        timerThread.start();
        //play sound
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        SoundPool soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();//stream 同時に扱う効果音の数
        int mp3 = getResources().getIdentifier("stopwatch", "raw", getPackageName());
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
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            finishedPomodoro();
        }

        public void stopRunning(){
            this.running = false;
        }
    }

    private void finishedPomodoro(){
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

        vm.registerWorkedPomo();//TODO 終了POPUP
        vm.isStarted.set(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!vm.isStarted.get()
                || keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            //戻るボタンは使えない
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("ポモドーロ中です")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
            return false;
        }
    }

    public void stopPomodoro(View view){
        vm.isShowReason.set(true);
    }

    public void hideReasonView(View view){
        vm.isShowReason.set(false);
    }

    public void registerReason(View view){
        String reason = ((TextView)findViewById(R.id.reason)).getText().toString();
        vm.registerReason(reason);
        ((TextView)findViewById(R.id.reason)).setText("");
        timerThread.stopRunning();
        vm.isShowReason.set(false);
        vm.isStarted.set(false);
    }

}
