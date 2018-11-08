package com.mochoi.pomer.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mochoi.pomer.databinding.BacklogItemBinding;
import com.mochoi.pomer.viewmodel.Task;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
    private List<Task> tasks;

    public TaskRecyclerViewAdapter(List<Task> items) {
        replaceData(items);
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

        // データセット
        holder.binding.setTask(task);

        // Viewへの反映を即座に行う
        holder.binding.executePendingBindings();
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

    class ViewHolder extends RecyclerView.ViewHolder {

        final BacklogItemBinding binding;

        public ViewHolder(BacklogItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
