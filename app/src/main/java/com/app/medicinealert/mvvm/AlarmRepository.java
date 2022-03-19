package com.app.medicinealert.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.medicinealert.database.DAO;
import com.app.medicinealert.database.MedicineAlertDataBase;
import com.app.medicinealert.models.AlarmModel;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AlarmRepository {
    private MutableLiveData<Integer> isDataChanged;
    private MutableLiveData<Integer> onDeleteSuccess;

    private MutableLiveData<List<AlarmModel>> onDataSuccess;
    private MedicineAlertDataBase dataBase;
    private DAO dao;
    private CompositeDisposable disposable = new CompositeDisposable();


    public AlarmRepository(Context context) {
        dataBase = MedicineAlertDataBase.newInstance(context.getApplicationContext());
        dao = dataBase.getDAO();
    }

    public MutableLiveData<Integer> getIsDataChanged() {
        if (isDataChanged == null) {
            isDataChanged = new MutableLiveData<>();
        }
        return isDataChanged;
    }

    public MutableLiveData<List<AlarmModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<Integer> getOnDeleteSuccess() {
        if (onDeleteSuccess == null) {
            onDeleteSuccess = new MutableLiveData<>();
        }
        return onDeleteSuccess;
    }


    public void insert(AlarmModel model, Context context) {
        dao.insert(model).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        if (getOnDataSuccess().getValue() != null) {
                            getOnDataSuccess().getValue().add(model);
                            getIsDataChanged().postValue(0);

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void update(AlarmModel model, Context context, int pos) {
        dao.update(model).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        if (getOnDataSuccess().getValue() != null) {
                            getOnDataSuccess().getValue().set(pos, model);
                            getIsDataChanged().postValue(pos);

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void delete(int alarm_id,String user_id, Context context) {
        dao.delete(alarm_id).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        getAlarmData(user_id,context);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getAlarmData(String user_id, Context context) {
        dao.getMyAlarm(user_id).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<AlarmModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<AlarmModel> alarmModels) {
                        getOnDataSuccess().postValue(alarmModels);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void clear() {
        disposable.clear();
    }

}
