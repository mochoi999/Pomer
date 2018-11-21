package com.mochoi.pomer;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PomerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        //TODO あとでけす
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }
}
