package com.mochoi.pomer.infra;

import com.mochoi.pomer.model.entity.ForecastPomo;
import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.repository.RemoveTaskRepository;

import io.realm.Realm;
import io.realm.RealmResults;


public class RemoveTaskRepositoryImpl implements RemoveTaskRepository {
    private Realm realm;

    public RemoveTaskRepositoryImpl(Realm realm){
        this.realm = realm;
    }

    public void removeTaskById(long id){
        realm.beginTransaction();
        RealmResults<ForecastPomo> forecastPomos = realm.where(ForecastPomo.class)
                .equalTo("task.id", id).findAll();
        forecastPomos.deleteAllFromRealm();

        RealmResults<WorkedPomo> workedPomos = realm.where(WorkedPomo.class)
                .equalTo("task.id", id).findAll();
        workedPomos.deleteAllFromRealm();

        RealmResults<Reason> reasons = realm.where(Reason.class)
                .equalTo("task.id", id).findAll();
        reasons.deleteAllFromRealm();

        RealmResults<Task> tasks = realm.where(Task.class).equalTo("id", id).findAll();
        tasks.deleteFirstFromRealm();

        realm.commitTransaction();

    }
}
