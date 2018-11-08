package com.mochoi.pomer.view;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mochoi.pomer.R;
import com.mochoi.pomer.contract.RegisterTaskNavigator;
import com.mochoi.pomer.databinding.RegisterTaskBinding;
import com.mochoi.pomer.viewmodel.RegisterTaskVM;
import com.mochoi.pomer.viewmodel.Task;

public class RegisterTaskActivity extends BaseActivity implements RegisterTaskNavigator {
    RegisterTaskVM registerTaskVM = new RegisterTaskVM();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RegisterTaskBinding registerTaskBinding = DataBindingUtil.setContentView(this, R.layout.register_task);
//        registerTaskVM.task.set(new Task());
        registerTaskVM.task.set(new Task());;
        ObservableField<String> aaa = new ObservableField<>();
        aaa.set("aaa");
        registerTaskVM.aaa = aaa;

                registerTaskVM.setNavigator(this);
        registerTaskBinding.setTaskVM(registerTaskVM);//layoutのnameにセット

    }

    @Override
    public void backActivity() {
        this.finish();
    }

    public void register(View view){
        registerTaskVM.register();
        showNotification("登録しました");
        registerTaskVM.task.set(new Task());
    }

}
