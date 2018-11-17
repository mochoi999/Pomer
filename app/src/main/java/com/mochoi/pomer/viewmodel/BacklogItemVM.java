package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import com.mochoi.pomer.model.entity.Task;

/**
 * バックログ画面のリストアイテム用ビューモデル
 */
public class BacklogItemVM {
    public final ObservableBoolean check = new ObservableBoolean(false);
    public final ObservableField<Task> task = new ObservableField<>();

}
