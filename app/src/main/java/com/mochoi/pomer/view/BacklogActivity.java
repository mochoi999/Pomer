package com.mochoi.pomer.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.BacklogMainBinding;
import com.mochoi.pomer.viewmodel.BacklogVM;
import com.mochoi.pomer.viewmodel.Task;

import java.util.ArrayList;

public class BacklogActivity extends BaseActivity {
    private BacklogVM vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BacklogMainBinding binding =  DataBindingUtil.setContentView(this, R.layout.backlog_main);

        vm = new BacklogVM();
        vm.setUpTaskList();
        binding.setBacklogVM(vm);

        TaskRecyclerViewAdapter adapter = new TaskRecyclerViewAdapter(new ArrayList<Task>());
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);

    }
}
