package com.mochoi.pomer.viewmodel;

import android.database.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mochoi.pomer.model.Task;

/**
 * Todoリスト画面のリストアイテム用ビューモデル
 */
public class TodolistItemVM {
    public final ObservableBoolean check = new ObservableBoolean(false);
    public ObservableField<Task> task = new ObservableField<>();
    public ObservableField<String> forecastPomo = new ObservableField<>();
    public ObservableField<String> workedPomo = new ObservableField<>();
}
