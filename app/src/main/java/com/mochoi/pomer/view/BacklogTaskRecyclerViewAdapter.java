package com.mochoi.pomer.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.BacklogItemBinding;
import com.mochoi.pomer.model.Task;
import com.mochoi.pomer.viewmodel.BacklogVM;

import android.util.Log;

import java.util.List;

public class BacklogTaskRecyclerViewAdapter extends RecyclerView.Adapter<BacklogTaskRecyclerViewAdapter.ViewHolder> {
    private BacklogActivity activity;
    private BacklogVM vm;
    private List<Task> tasks;

    public BacklogTaskRecyclerViewAdapter(BacklogActivity activity, BacklogVM vm){
        this.activity = activity;
        this.vm = vm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // DataBinding
        BacklogItemBinding binding = BacklogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.binding.setTask(task);
    }

    @Override
    public int getItemCount() {
        if(tasks == null){
            return 0;
        } else {
            return tasks.size();
        }
    }

    public void replaceData(List<Task> items) {
        setList(items);
    }
    private void setList(List<Task> items) {
        this.tasks = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final BacklogItemBinding binding;

        public ViewHolder(BacklogItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.findViewById(R.id.remove).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            vm.removeTask(binding.getTask().id);
            activity.showNotification("削除しました");
            vm.setUpTaskList();
        }
    }
}
