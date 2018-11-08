package com.mochoi.pomer.view;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.mochoi.pomer.viewmodel.Task;

import java.util.List;

/**
 * 自作BindingAdapter
 */
public class BindingAdapters {
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, List<Task> tasks) {
        TaskRecyclerViewAdapter adapter = (TaskRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(tasks);
        } else {
            adapter = new TaskRecyclerViewAdapter(tasks);
            recyclerView.setAdapter(adapter);
        }
    }
}
