package com.app.medicinealert.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.app.medicinealert.R;
import com.app.medicinealert.alarm_broadcasat.AlarmReceiver;
import com.app.medicinealert.preferences.Preferences;
import com.app.medicinealert.tags.App;

public class AlarmService extends Service {
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = Preferences.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean canShowNotification = preferences.getShowNotification(this);
        if (canShowNotification) {
            createNotification(intent.getIntExtra(AlarmReceiver.ALARM_ID, 0), intent.getStringExtra(AlarmReceiver.TITLE), intent.getStringExtra(AlarmReceiver.DOSAGE), intent.getStringExtra(AlarmReceiver.NOTE));
        }
        fireActivityRingtone(intent);
        stopSelf();
        return START_STICKY;
    }

    private void fireActivityRingtone(Intent intent) {

    }

    private void createNotification(int alarm_id, String medicine_name, String dosage, String note) {
        String body = getString(R.string.dosage) + ":" + dosage + "\n" + note;

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setAutoCancel(true)
                .setOngoing(true)
                .setChannelId(App.CHANNEL_ID)
                .setContentTitle(medicine_name)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationCompat.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

        manager.notify(alarm_id, notificationCompat.build());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
