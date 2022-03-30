package com.app.medicinealert.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.medicinealert.models.AlarmModel;
import com.app.medicinealert.models.ReportModel;

import java.util.List;

public class ReportMvvm extends AndroidViewModel {
    private ReportRepository repository;
    private MutableLiveData<Boolean> onDataInsertedSuccess;
    private MutableLiveData<List<ReportModel>> onDataSuccess;
    private MutableLiveData<Boolean> refreshData;


    public ReportMvvm(@NonNull Application application) {
        super(application);
        repository = new ReportRepository(application);
        onDataSuccess = repository.getOnDataSuccess();
        onDataInsertedSuccess = repository.getOnDataInsertedSuccess();

    }

    public MutableLiveData<Boolean> getOnDataInsertedSuccess() {
        if (onDataInsertedSuccess == null) {
            onDataInsertedSuccess = new MutableLiveData<>();
        }
        return onDataInsertedSuccess;
    }


    public void insert(ReportModel model, Context context) {
        repository.insert(model, context);
    }

    public void getReportData(String user_id, Context context) {
        repository.getReportData(user_id, context);
        onDataSuccess = repository.getOnDataSuccess();
    }



    public MutableLiveData<List<ReportModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        repository.clear();
    }
}
