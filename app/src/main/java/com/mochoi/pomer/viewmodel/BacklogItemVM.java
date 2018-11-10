package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import com.mochoi.pomer.model.Task;

import java.util.List;

public class BacklogItemVM {
    public final ObservableBoolean check = new ObservableBoolean(false);
    public final ObservableField<Task> task = new ObservableField<>();

}
