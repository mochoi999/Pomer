package com.mochoi.pomer.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.BacklogMainBinding;
import com.mochoi.pomer.viewmodel.BacklogVM;

import android.util.Log;

/**
 * バックログ画面用アクティビティ
 */
public class BacklogActivity extends BaseActivity {
    private BacklogVM vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BacklogMainBinding binding = DataBindingUtil.setContentView(this, R.layout.backlog_main);

        vm = new BacklogVM();
        vm.setUpTaskList();
        binding.setBacklogVM(vm);

        BacklogTaskRecyclerViewAdapter adapter = new BacklogTaskRecyclerViewAdapter(this, vm);
        RecyclerView recyclerView = binding.recycler;
        //data set
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        vm.setUpTaskList();
    }

    public void moveEditActivity(long id) {
        Intent intent = new Intent(this, RegisterEditTaskActivity.class);
        intent.putExtra("mode", RegisterEditTaskActivity.MODE.EDIT.getId());
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void moveTaskToTodo(View view){
        CheckBox checkBox = findViewById(R.id.checkBox);
        Log.d("TEST", ""+checkBox.isChecked());
    }
}
