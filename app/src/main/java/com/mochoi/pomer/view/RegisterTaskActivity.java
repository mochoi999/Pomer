package com.mochoi.pomer.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.RegisterTaskBinding;
import com.mochoi.pomer.viewmodel.RegisterTaskVM;
import com.mochoi.pomer.model.Task;

public class RegisterTaskActivity extends BaseActivity {
    RegisterTaskVM registerTaskVM = new RegisterTaskVM();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RegisterTaskBinding registerTaskBinding = DataBindingUtil.setContentView(this, R.layout.register_task);
        registerTaskVM.task.set(new Task());
        registerTaskBinding.setTaskVM(registerTaskVM);//layoutのnameにセット

    }

    public void backActivity(View view) {
        this.finish();
    }

    public void register(View view){
        registerTaskVM.register();
        showNotification("登録しました");
        registerTaskVM.task.set(new Task());
    }

}
