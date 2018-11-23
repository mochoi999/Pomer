package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.entity.Task;

public class PastItemVM {
    public final ObservableField<Task> task = new ObservableField<>();

}
