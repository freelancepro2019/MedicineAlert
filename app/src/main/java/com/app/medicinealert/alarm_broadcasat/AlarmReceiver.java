package com.app.medicinealert.alarm_broadcasat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.app.medicinealert.mvvm.AlarmRepository;
import com.app.medicinealert.services.AlarmService;
import com.app.medicinealert.services.RescheduleAlarmService;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String RECURRING = "recurring";
    public static final String SAT = "sat";
    public static final String SUN = "sun";
    public static final String MON = "mon";
    public static final String TUS = "tus";
    public static final String WED = "wed";
    public static final String THU = "thu";
    public static final String FRI = "fri";
    public static final String TIME = "time";
    public static final String TITLE = "title";
    public static final String DOSAGE = "dosage";
    public static final String NOTE = "note";
    public static final String USER_ID = "user_id";
    public static final String ALARM_ID = "alarm_id";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            startRescheduleAlarmService(context);
        } else {
            if (intent.getBooleanExtra(RECURRING, false)) {
                if (alarmIsToday(intent)) {
                    Log.e("2", "2");
                    startAlarmService(intent, context);
                }
            } else {
                Log.e("1", "1");
                startAlarmService(intent, context);

            }
        }
    }

    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch (today) {
            case Calendar.MONDAY:
                if (intent.getBooleanExtra(MON, false))
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (intent.getBooleanExtra(TUS, false))
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (intent.getBooleanExtra(WED, false))
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (intent.getBooleanExtra(THU, false))
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (intent.getBooleanExtra(FRI, false))
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (intent.getBooleanExtra(SAT, false))
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (intent.getBooleanExtra(SUN, false))
                    return true;
                return false;
        }
        return false;
    }

    private void startAlarmService(Intent intent, Context context) {
        String medicine_name = intent.getStringExtra(TITLE);
        String dosage = intent.getStringExtra(DOSAGE);
        String time = intent.getStringExtra(TIME);
        String note = intent.getStringExtra(NOTE);
        String user_id = intent.getStringExtra(USER_ID);
        int alarm_id = intent.getIntExtra(ALARM_ID,0);


        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra(TITLE, medicine_name);
        intentService.putExtra(DOSAGE, dosage);
        intentService.putExtra(TIME, time);
        intentService.putExtra(NOTE, note);
        intentService.putExtra(USER_ID, user_id);
        intentService.putExtra(ALARM_ID, alarm_id);

        context.startService(intentService);

        boolean isRecurring = intent.getBooleanExtra(RECURRING,false);
        if (!isRecurring){
            AlarmRepository repository = new AlarmRepository(context.getApplicationContext());
            repository.delete(alarm_id,user_id,context);
            EventBus.getDefault().post("refresh");
        }

    }

    private void startRescheduleAlarmService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmService.class);
        String user_id = intentService.getStringExtra(USER_ID);
        intentService.putExtra(USER_ID, user_id);
        context.startService(intentService);

    }


}
