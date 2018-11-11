package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;

import com.mochoi.pomer.model.Task;

/**
 * Todoリスト画面のリストアイテム用ビューモデル
 */
public class TodolistItemVM {
    public ObservableField<Task> task = new ObservableField<>();
}
