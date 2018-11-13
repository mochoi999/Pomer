package com.mochoi.pomer.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.TimerMainBinding;
import com.mochoi.pomer.viewmodel.TimerVM;

/**
 * タイマー画面用アクティビティ
 */
public class TimerActivity extends BaseActivity {
    private TimerVM vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getIntent().getLongExtra("id", 0);

        TimerMainBinding binding = DataBindingUtil.setContentView(this, R.layout.timer_main);
        vm = new TimerVM();
        vm.setUpTaskData(id);
        binding.setTimerVM(vm);
    }

    public void startTimer(View view){
        vm.isStarted.set(true);
        new TimerThread().start();
        //play sound
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        SoundPool soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();//stream 同時に扱う効果音の数
        int mp3 = getResources().getIdentifier("stopwatch_start", "raw", getPackageName());
        final int soundID = soundPool.load(getBaseContext(), mp3, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundID,1f, 1f, 0, 0, 1);
            }
        });

    }
    private class TimerThread extends Thread {
        public void run() {
            int len = vm.time.get();
            for (int i = len-1; i >-1; i--) {
                try {
                    Thread.sleep(60 * 1000);
                    vm.time.set(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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

            vm.isStarted.set(false);

        }
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

}
