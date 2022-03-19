package com.app.medicinealert.mvvm;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.app.medicinealert.models.AlarmModel;

import java.util.ArrayList;
import java.util.List;

public class AlarmMvvm extends AndroidViewModel {
    private AlarmRepository repository;
    private MutableLiveData<Integer> isDataChanged;
    private MutableLiveData<Integer> onDeleteSuccess;
    private MutableLiveData<List<AlarmModel>> onDataSuccess;
    private MutableLiveData<Boolean> refreshData;


    public AlarmMvvm(@NonNull Application application) {
        super(application);
        repository = new AlarmRepository(application);
        isDataChanged = repository.getIsDataChanged();
        onDataSuccess = repository.getOnDataSuccess();
        onDeleteSuccess = repository.getOnDeleteSuccess();


    }


    public void insert(AlarmModel model, Context context) {
        repository.insert(model, context);
    }

    public void update(AlarmModel model, Context context, int pos) {
        repository.update(model, context, pos);
    }

    public void delete(int alarm_id,String user_id, Context context) {
        repository.delete(alarm_id,user_id, context);
    }

    public void getAlarmData(String user_id, Context context) {
        repository.getAlarmData(user_id, context);
        onDataSuccess = repository.getOnDataSuccess();
    }

    public MutableLiveData<Integer> getIsDataChanged() {
        if (isDataChanged == null) {
            isDataChanged = new MutableLiveData<>();
        }
        return isDataChanged;
    }

    public MutableLiveData<Boolean> onRefresh() {
        if (refreshData == null) {
            refreshData = new MutableLiveData<>();
        }
        return refreshData;
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



    @Override
    protected void onCleared() {
        super.onCleared();
        repository.clear();
    }
}
