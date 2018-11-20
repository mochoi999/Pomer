package com.mochoi.pomer.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.RegistereditTaskBinding;
import com.mochoi.pomer.di.DaggerAppComponent;
import com.mochoi.pomer.model.vo.TaskKind;
import com.mochoi.pomer.viewmodel.RegisterEditTaskVM;
import com.mochoi.pomer.model.entity.Task;

import org.apache.commons.lang3.StringUtils;

/**
 * タスク登録・更新画面用アクティビティ
 */
public class RegisterEditTaskActivity extends BaseActivity {
//    private RegisterEditTaskVM registerEditTaskVM = new RegisterEditTaskVM();
    private RegisterEditTaskVM registerEditTaskVM = DaggerAppComponent.create().makeRegisterEditTaskVM();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RegistereditTaskBinding registerTaskBinding = DataBindingUtil.setContentView(this, R.layout.registeredit_task);
        registerEditTaskVM.task.set(new Task());
        registerTaskBinding.setTaskVM(registerEditTaskVM);//layoutのnameにセット

        //modeの設定
        int mode = getIntent().getIntExtra("mode", MODE.REGIST.getId());
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

    public void registerBacklog(View view){
        register(TaskKind.BackLog);
    }

    public void registerTodolist(View view){
        register(TaskKind.ToDoToday);
    }

    private void register(TaskKind taskKind){
        if(!validateInputData()){
            return;
        }
        registerEditTaskVM.forecastPomo.set(convertForecastPomoCnt(registerEditTaskVM.forecastPomo.get()));
        registerEditTaskVM.register(taskKind);
        showNotification("登録しました");
        registerEditTaskVM.task.set(new Task());
        registerEditTaskVM.forecastPomo.set("");
        findViewById(R.id.task_name).requestFocus();
    }

    private String convertForecastPomoCnt(String cnt){
        if(StringUtils.isBlank(cnt) || "0".equals(cnt)){
            return "1";
        } else {
            return cnt;
        }
    }

    private void setTaskData(long id){
        Task task = registerEditTaskVM.getTaskDataById(id);
        registerEditTaskVM.task.set(task);
        registerEditTaskVM.forecastPomo.set(task.forecastPomos.last().pomodoroCount);
    }

    public void modify(View view){
        if(!validateInputData()){
            return;
        }
        registerEditTaskVM.modifyById();
        showNotification("更新しました");
        this.finish();
    }

}
