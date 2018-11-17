package com.mochoi.pomer.view;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.mochoi.pomer.viewmodel.BacklogItemVM;
import com.mochoi.pomer.viewmodel.TodolistItemVM;

import java.util.List;

/**
 * 自作BindingAdapter
 */
public class BindingAdapters {
    @BindingAdapter("backlog_items")
    public static void setBacklogItems(RecyclerView recyclerView, List<BacklogItemVM> items) {
        BacklogTaskRecyclerViewAdapter adapter = (BacklogTaskRecyclerViewAdapter) recyclerView.getAdapter();
        adapter.replaceData(items);
    }

    @BindingAdapter("todo_items")
    public static void setTodoItems(RecyclerView recyclerView, List<TodolistItemVM> items) {
        TodoListActivity.RecyclerViewAdapter adapter = (TodoListActivity.RecyclerViewAdapter) recyclerView.getAdapter();
        adapter.replaceData(items);
    }
}
