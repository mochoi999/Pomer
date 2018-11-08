package com.mochoi.pomer.view;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mochoi.pomer.R;
import com.mochoi.pomer.contract.RegisterTaskNavigator;
import com.mochoi.pomer.databinding.RegisterTaskBinding;
import com.mochoi.pomer.viewmodel.RegisterTaskVM;
import com.mochoi.pomer.viewmodel.Task;

public class RegisterTaskActivity extends AppCompatActivity implements RegisterTaskNavigator {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RegisterTaskBinding registerTaskBinding = DataBindingUtil.setContentView(this, R.layout.register_task);
        RegisterTaskVM registerTaskVM = new RegisterTaskVM();
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
}
