package com.mochoi.pomer;

import android.app.Application;

import io.realm.Realm;

public class PomerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
