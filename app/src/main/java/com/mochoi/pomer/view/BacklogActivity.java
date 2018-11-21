package com.mochoi.pomer.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.BacklogMainBinding;
import com.mochoi.pomer.di.DaggerAppComponent;
import com.mochoi.pomer.viewmodel.BacklogItemVM;
import com.mochoi.pomer.viewmodel.BacklogVM;

import java.util.ArrayList;
import java.util.List;

/**
 * バックログ画面用アクティビティ
 */
public class BacklogActivity extends BaseActivity {
    private BacklogVM vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BacklogMainBinding binding = DataBindingUtil.setContentView(this, R.layout.backlog_main);

        vm = DaggerAppComponent.create().makeBacklogVM();
        vm.refreshTaskList();
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
        vm.refreshTaskList();
    }

    public void moveEditActivity(long id) {
        Intent intent = new Intent(this, RegisterEditTaskActivity.class);
        intent.putExtra("mode", RegisterEditTaskActivity.MODE.EDIT.getId());
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void moveTaskToTodo(View view){
         List<BacklogItemVM> items = vm.items.get();
         List<Long> ids = new ArrayList<>();
         for(BacklogItemVM item : items){
             if(item.check.get()){
                 ids.add(item.task.get().id);
             }
         }

         if(ids.size() == 0){
             showNotification("移動するタスクをチェックしてください");
             return;
         }

         vm.modifyBacklog2Todo(ids.toArray(new Long[ids.size()]));
         showNotification("移動しました");
         vm.refreshTaskList();
    }
}
