package com.app.medicinealert.mvvm;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.medicinealert.database.DAO;
import com.app.medicinealert.database.MedicineAlertDataBase;
import com.app.medicinealert.models.AlarmModel;
import com.app.medicinealert.models.ReportModel;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReportRepository {
    private MutableLiveData<List<ReportModel>> onDataSuccess;
    private MutableLiveData<Boolean> onDataInsertedSuccess;

    private MedicineAlertDataBase dataBase;
    private DAO dao;
    private CompositeDisposable disposable = new CompositeDisposable();


    public ReportRepository(Context context) {
        dataBase = MedicineAlertDataBase.newInstance(context.getApplicationContext());
        dao = dataBase.getDAO();
    }


    public MutableLiveData<List<ReportModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<Boolean> getOnDataInsertedSuccess() {
        if (onDataInsertedSuccess == null) {
            onDataInsertedSuccess = new MutableLiveData<>();
        }
        return onDataInsertedSuccess;
    }





    public void insert(ReportModel model, Context context) {
        dao.insertReport(model).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        if (getOnDataSuccess().getValue() != null) {
                            getOnDataInsertedSuccess().postValue(true);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getReportData(String user_id, Context context) {
        dao.getReports(user_id).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ReportModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<ReportModel> reportModels) {
                        getOnDataSuccess().postValue(reportModels);

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
