package com.mochoi.pomer.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.BacklogItemBinding;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.viewmodel.BacklogItemVM;
import com.mochoi.pomer.viewmodel.BacklogVM;


import java.util.List;

/**
 * バックログ画面のRecyclerView用アダプター
 */
public class BacklogTaskRecyclerViewAdapter extends RecyclerView.Adapter<BacklogTaskRecyclerViewAdapter.ViewHolder> {
    private BacklogActivity activity;
    private BacklogVM vm;
    private List<BacklogItemVM> items;

    BacklogTaskRecyclerViewAdapter(BacklogActivity activity, BacklogVM vm){
        this.activity = activity;
        this.vm = vm;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // DataBinding
        BacklogItemBinding binding = BacklogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BacklogItemVM item = items.get(position);
        //TODO
        Task task = item.task.get();
        item.forecastPomo.set(task.forecastPomos.last().pomodoroCount);
        item.workedPomo.set(String.valueOf(task.workedPomos.size()));

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

    void replaceData(List<BacklogItemVM> items) {
        setList(items);
    }
    private void setList(List<BacklogItemVM> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        final BacklogItemBinding binding;

        ViewHolder(final BacklogItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.findViewById(R.id.taskName).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.moveEditActivity(binding.getItem().task.get().id);
                }
            });
            itemView.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("タスクを削除します。よろしいですか？")
                            .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    vm.removeTask(binding.getItem().task.get().id);
                                    activity.showNotification("削除しました");
                                    vm.refreshTaskList();
                                }
                            });
                    builder.show();
                }
            });

        }


    }
}
