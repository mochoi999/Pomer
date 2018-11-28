package com.mochoi.pomer.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.SettingMainBinding;
import com.mochoi.pomer.model.vo.IdSharedPreference;
import com.mochoi.pomer.model.vo.NameSharedPreference;
import com.mochoi.pomer.viewmodel.SettingVM;

import org.apache.commons.lang3.StringUtils;

public class SettingActivity extends BaseActivity {
    private SettingVM vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);

        // アクションバーをカスタマイズ
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("設定");

        SettingMainBinding binding = DataBindingUtil.setContentView(this, R.layout.setting_main);
        vm = new SettingVM();
        setDataWithSharedPreferences();
        binding.setSettingVM(vm);

    }

    private void setDataWithSharedPreferences(){
        //ポモドーロ時間
        SharedPreferences pref = getSharedPreferences(NameSharedPreference.Setting.getValue(), Context.MODE_PRIVATE);
        int pomodoroTime = pref.getInt(IdSharedPreference.SettingPomodoroTime.getValue(), 25);
        vm.pomodoroTime.set(String.valueOf(pomodoroTime));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                if(registerSettings()) {
                    finish();
                    return true;
                } else {
                    return false;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            if(registerSettings()) {
                finish();
                return true;
            } else {
                return false;
            }
        }
    }


    private boolean validateInputData(){
        if(StringUtils.isBlank(vm.pomodoroTime.get())){
            showNotification("ポモドーロ時間を入力してください");
            return false;
        }
        if(Integer.parseInt(vm.pomodoroTime.get()) < 0){
            showNotification("ポモドーロ時間は0以上を入力してください");
            return false;
        }
        return true;
    }

    private boolean registerSettings(){
        if(!validateInputData()) {
            return false;
        }

        SharedPreferences pref = getSharedPreferences(NameSharedPreference.Setting.getValue(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //ポモドーロ時間
        editor.putInt(IdSharedPreference.SettingPomodoroTime.getValue(), Integer.parseInt(vm.pomodoroTime.get()));

        editor.apply();
        return true;
    }

}
