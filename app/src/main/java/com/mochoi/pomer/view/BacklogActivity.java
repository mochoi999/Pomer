package com.mochoi.pomer.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.BacklogMainBinding;
import com.mochoi.pomer.viewmodel.BacklogVM;

/**
 * バックログ画面用アクティビティ
 */
public class BacklogActivity extends BaseActivity {
    private BacklogVM vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BacklogMainBinding binding =  DataBindingUtil.setContentView(this, R.layout.backlog_main);

        vm = new BacklogVM();
        vm.setUpTaskList();
        binding.setBacklogVM(vm);

        BacklogTaskRecyclerViewAdapter adapter = new BacklogTaskRecyclerViewAdapter(vm);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        vm.setUpTaskList();
    }
}
