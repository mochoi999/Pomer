package com.aklob.pomer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.aklob.pomer.contract.RegisterTaskNavigator;
import com.aklob.pomer.databinding.RegisterTaskBinding;
import com.aklob.pomer.viewmodel.RegisterTaskVM;
import com.aklob.pomer.viewmodel.Task;

public class RegisterTaskActivity extends AppCompatActivity implements RegisterTaskNavigator {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RegisterTaskBinding registerTaskBinding = DataBindingUtil.setContentView(this, R.layout.register_task);
        RegisterTaskVM registerTaskVM = new RegisterTaskVM();
        registerTaskVM.task.set(new Task());
        registerTaskVM.setNavigator(this);
        registerTaskBinding.setTaskVM(registerTaskVM);//layoutのnameにセット

    }

    @Override
    public void backActivity() {
        this.finish();
    }
}
