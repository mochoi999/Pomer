package com.mochoi.pomer.model;

import io.realm.Realm;

/**
 * 完了できなかったタスクの理由の操作用サービス
 */
public class NotFinishedReasonService {
    Realm realm;

    public NotFinishedReasonService(){
        realm = Realm.getDefaultInstance();
    }

    public void register(NotFinishedReason reason){
        realm.beginTransaction();
        Number maxid = realm.where(NotFinishedReason.class).max("id");
        if (maxid == null){
            maxid = 0;
        }

        reason.id = maxid.longValue() + 1;
        realm.copyToRealm(reason);
        realm.commitTransaction();
    }
}
