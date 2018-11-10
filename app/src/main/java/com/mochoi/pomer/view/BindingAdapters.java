package com.mochoi.pomer.view;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.mochoi.pomer.model.Task;
import com.mochoi.pomer.viewmodel.BacklogItemVM;

import java.util.List;

/**
 * 自作BindingAdapter
 */
public class BindingAdapters {
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, List<BacklogItemVM> items) {
        BacklogTaskRecyclerViewAdapter adapter = (BacklogTaskRecyclerViewAdapter) recyclerView.getAdapter();
        adapter.replaceData(items);
    }
}
