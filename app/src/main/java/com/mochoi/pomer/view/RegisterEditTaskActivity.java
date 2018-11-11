package com.mochoi.pomer.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.RegistereditTaskBinding;
import com.mochoi.pomer.viewmodel.RegisterEditTaskVM;
import com.mochoi.pomer.model.Task;

import org.apache.commons.lang3.StringUtils;

/**
 * タスク登録・更新画面用アクティビティ
 */
public class RegisterEditTaskActivity extends BaseActivity {
    private RegisterEditTaskVM registerEditTaskVM = new RegisterEditTaskVM();

    /**
     * 画面操作モード　登録　更新
     */
    public enum MODE {
        REGIST(1),EDIT(2);
        private int id;
        MODE(int id){
            this.id = id;
        }
        public int getId() {
            return id;
        }
    }
    public int mode = MODE.REGIST.getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RegistereditTaskBinding registerTaskBinding = DataBindingUtil.setContentView(this, R.layout.registeredit_task);
        registerEditTaskVM.task.set(new Task());
        registerTaskBinding.setTaskVM(registerEditTaskVM);//layoutのnameにセット

        mode = getIntent().getIntExtra("mode", MODE.REGIST.getId());
        registerEditTaskVM.isRegisterMode.set(MODE.REGIST.getId() == mode);
        if(!registerEditTaskVM.isRegisterMode.get()){
            setTaskData(getIntent().getLongExtra("id", 0));
        }

    }

    public void backActivity(View view) {
        this.finish();
    }

    private boolean validateInputData(){
        if(StringUtils.isEmpty(registerEditTaskVM.task.get().taskName)){
            showNotification("タスク名を入力してください");
            return false;
        }
        return true;
    }

    public void register(View view){
        if(!validateInputData()){
            return;
        }
        registerEditTaskVM.register();
        showNotification("登録しました");
        registerEditTaskVM.task.set(new Task());
    }

    private void setTaskData(long id){
        Task task = registerEditTaskVM.getTaskDataById(id);
        registerEditTaskVM.task.set(task);
    }

    public void modify(View view){
        if(!validateInputData()){
            return;
        }
        registerEditTaskVM.modifyById();
        showNotification("更新しました");
    }

}
