package com.app.medicinealert.services;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import com.app.medicinealert.alarm_broadcasat.AlarmReceiver;
import com.app.medicinealert.models.AlarmModel;
import com.app.medicinealert.models.UserModel;
import com.app.medicinealert.mvvm.AlarmRepository;
import com.app.medicinealert.preferences.Preferences;

import java.util.List;

import io.reactivex.Observer;

public class RescheduleAlarmService extends LifecycleService {
    private Preferences preferences;
    private UserModel userModel;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        AlarmRepository alarmRepository = new AlarmRepository(getApplication());
        alarmRepository.getAlarmData(userModel.getUser_id(), getApplicationContext());
        alarmRepository.getOnDataSuccess().observe(this, List -> {
            for (AlarmModel a : List) {
                if (a.isStarted()) {
                    a.schedule(getApplicationContext());
                }
            }
        });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
