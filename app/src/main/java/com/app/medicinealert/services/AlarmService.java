package com.app.medicinealert.services;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.app.medicinealert.R;
import com.app.medicinealert.alarm_broadcasat.AlarmReceiver;
import com.app.medicinealert.preferences.Preferences;
import com.app.medicinealert.tags.App;
import com.app.medicinealert.uis.activity_ringtone.RingtoneActivity;

public class AlarmService extends Service {
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = Preferences.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //boolean canShowNotification = preferences.getShowNotification(this);
        createNotification(intent.getIntExtra(AlarmReceiver.ALARM_ID, 0),intent.getStringExtra(AlarmReceiver.MEDICINE_ID), intent.getStringExtra(AlarmReceiver.TITLE), intent.getStringExtra(AlarmReceiver.DOSAGE), intent.getStringExtra(AlarmReceiver.NOTE));
        fireActivityRingtone(intent);
        return START_STICKY;
    }

    private void fireActivityRingtone(Intent intent) {
        Log.e("ring","fired");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        int alarm_id = intent.getIntExtra(AlarmReceiver.ALARM_ID, 0);
        String title = intent.getStringExtra(AlarmReceiver.TITLE);
        String note = intent.getStringExtra(AlarmReceiver.NOTE);
        String dosage = intent.getStringExtra(AlarmReceiver.DOSAGE);
        String medicine_id = intent.getStringExtra(AlarmReceiver.MEDICINE_ID);

        Intent intent2 = new Intent(getApplicationContext(), RingtoneActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra(AlarmReceiver.ALARM_ID, alarm_id);
        intent2.putExtra(AlarmReceiver.TITLE, title);
        intent2.putExtra(AlarmReceiver.NOTE, note);
        intent2.putExtra(AlarmReceiver.DOSAGE, dosage);
        intent2.putExtra(AlarmReceiver.MEDICINE_ID, medicine_id);
        startActivity(intent2);
        wl.release();

    }

    private void createNotification(int alarm_id,String medicine_id,String medicine_name, String dosage, String note) {
        String body = getString(R.string.dosage) + ":" + dosage + "\n" + note;

        Intent intent = new Intent(getApplicationContext(), RingtoneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(AlarmReceiver.ALARM_ID, alarm_id);
        intent.putExtra(AlarmReceiver.TITLE, medicine_name);
        intent.putExtra(AlarmReceiver.NOTE, note);
        intent.putExtra(AlarmReceiver.DOSAGE, dosage);
        intent.putExtra(AlarmReceiver.MEDICINE_ID, medicine_id);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pendingIntent)
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
