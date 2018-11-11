package com.mochoi.pomer.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.TodolistItemBinding;
import com.mochoi.pomer.databinding.TodolistMainBinding;
import com.mochoi.pomer.model.RegisterModTaskService;
import com.mochoi.pomer.viewmodel.BacklogItemVM;
import com.mochoi.pomer.viewmodel.TodolistItemVM;
import com.mochoi.pomer.viewmodel.TodolistVM;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

/**
 * Todoリスト画面用アクティビティ
 */
public class TodoListActivity extends BaseActivity {
    private TodolistVM vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TodolistMainBinding binding = DataBindingUtil.setContentView(this, R.layout.todolist_main);

        vm = new TodolistVM();
        vm.setUpTaskList();
        binding.setTodolistVM(vm);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
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

    public void moveBacklog(View view){
        Intent intent = new Intent(this, BacklogActivity.class);
        startActivity(intent);
    }

    void removeTask(final long id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("タスクを削除します。よろしいですか？")
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        vm.removeTask(id);
                        showNotification("削除しました");
                        vm.setUpTaskList();
                    }
                });
        builder.show();
    }

    public void moveTaskToBacklog(View view){
        List<TodolistItemVM> items = vm.items.get();
        List<Long> ids = new ArrayList<>();
        for(TodolistItemVM item : items){
            if(item.check.get()){
                ids.add(item.task.get().id);
            }
        }

        if(ids.size() == 0){
            showNotification("移動するタスクをチェックしてください");
            return;
        }

        vm.modifyTodo2Backlog(ids.toArray(new Long[ids.size()]));
        showNotification("バックログに戻しました");
        vm.setUpTaskList();
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private List<TodolistItemVM> items;
        private TodoListActivity activity;

        public RecyclerViewAdapter(TodoListActivity activity){
            this.activity = activity;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // DataBinding
            TodolistItemBinding binding = TodolistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new RecyclerViewAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            TodolistItemVM item = items.get(position);
            holder.binding.setItem(item);
        }

        @Override
        public int getItemCount() {
            if(items == null){
                return 0;
            } else {
                return items.size();
            }
        }

        public void replaceData(List<TodolistItemVM> items) {
            setList(items);
        }
        private void setList(List<TodolistItemVM> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            final TodolistItemBinding binding;

            public ViewHolder(final TodolistItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

                itemView.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       activity.removeTask(binding.getItem().task.get().id);
                    }
                });

                itemView.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("TEST","--------------");
                    }
                });
            }


        }
    }

}
