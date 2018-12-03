package com.mochoi.pomer.view;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.mochoi.pomer.viewmodel.BacklogItemVM;
import com.mochoi.pomer.viewmodel.PastItemVM;
import com.mochoi.pomer.viewmodel.ReportDetailItemVM;
import com.mochoi.pomer.viewmodel.TodolistItemVM;

import java.util.List;

/**
 * 自作BindingAdapter
 */
public class BindingAdapters {
    @BindingAdapter("backlog_items")
    public static void setBacklogItems(RecyclerView recyclerView, List<BacklogItemVM> items) {
        BacklogTaskRecyclerViewAdapter adapter = (BacklogTaskRecyclerViewAdapter) recyclerView.getAdapter();
        if(adapter != null) {
            adapter.replaceData(items);
        }
    }

    @BindingAdapter("todo_items")
    public static void setTodoItems(RecyclerView recyclerView, List<TodolistItemVM> items) {
        TodoListActivity.RecyclerViewAdapter adapter = (TodoListActivity.RecyclerViewAdapter) recyclerView.getAdapter();
        if(adapter != null) {
            adapter.replaceData(items);
        }
    }

    @BindingAdapter("past_items")
    public static void setPastItems(RecyclerView recyclerView, List<PastItemVM> items) {
        PastActivity.RecyclerViewAdapter adapter = (PastActivity.RecyclerViewAdapter) recyclerView.getAdapter();
        if(adapter != null) {
            adapter.replaceData(items);
        }
    }

    @BindingAdapter("detail_items")
    public static void setDetailItems(RecyclerView recyclerView, List<ReportDetailItemVM> items) {
        ReportActivity.DetailRecyclerViewAdapter adapter = (ReportActivity.DetailRecyclerViewAdapter) recyclerView.getAdapter();
        if(adapter != null) {
            adapter.replaceData(items);
        }
    }
}
