package com.app.medicinealert.models;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.app.medicinealert.alarm_broadcasat.AlarmReceiver;

import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "alarm_table")
public class AlarmModel implements Serializable {
    @PrimaryKey
    @NonNull
    private int alarm_id;
    private String medicine_id;
    private String user_id;
    private String medicine_name;
    private String medicine_dosage;
    private int hour;
    private int minute;
    private String time;
    private String note;
    private boolean isStarted;
    private boolean recurring;
    private boolean isSat;
    private boolean isSun;
    private boolean isMon;
    private boolean isTue;
    private boolean isWed;
    private boolean isThu;
    private boolean isFri;



    public AlarmModel(int alarm_id, int hour, int minute, String medicine_id, String user_id, String medicine_name, String medicine_dosage, String time, String note, boolean recurring, boolean isSat, boolean isSun, boolean isMon, boolean isTue, boolean isWed, boolean isThu, boolean isFri, boolean isStarted) {
        this.alarm_id = alarm_id;
        this.medicine_id = medicine_id;
        this.hour = hour;
        this.minute = minute;
        this.user_id = user_id;
        this.medicine_name = medicine_name;
        this.medicine_dosage = medicine_dosage;
        this.recurring = recurring;
        this.isStarted = isStarted;
        this.time = time;
        this.note = note;
        this.isSat = isSat;
        this.isSun = isSun;
        this.isMon = isMon;
        this.isTue = isTue;
        this.isWed = isWed;
        this.isThu = isThu;
        this.isFri = isFri;
    }

    @SuppressLint("MissingPermission")
    public void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.RECURRING, recurring);
        intent.putExtra(AlarmReceiver.SAT, isSat);
        intent.putExtra(AlarmReceiver.MON, isMon);
        intent.putExtra(AlarmReceiver.TUS, isTue);
        intent.putExtra(AlarmReceiver.WED, isWed);
        intent.putExtra(AlarmReceiver.THU, isThu);
        intent.putExtra(AlarmReceiver.FRI, isFri);
        intent.putExtra(AlarmReceiver.TITLE, medicine_name);
        intent.putExtra(AlarmReceiver.DOSAGE, medicine_dosage);
        intent.putExtra(AlarmReceiver.TIME, time);
        intent.putExtra(AlarmReceiver.NOTE, note);
        intent.putExtra(AlarmReceiver.USER_ID, user_id);
        intent.putExtra(AlarmReceiver.ALARM_ID, alarm_id);
        intent.putExtra(AlarmReceiver.MEDICINE_ID, medicine_id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm_id,intent,0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        if (calendar.getTimeInMillis()<=System.currentTimeMillis()){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }

        if (isRecurring()){
            long DAILY = 1000*60*60*24;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),DAILY,pendingIntent);

        }else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }


    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarm_id, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        this.isStarted = false;


    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getMedicine_dosage() {
        return medicine_dosage;
    }

    public void setMedicine_dosage(String medicine_dosage) {
        this.medicine_dosage = medicine_dosage;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public boolean isSat() {
        return isSat;
    }

    public void setSat(boolean sat) {
        isSat = sat;
    }

    public boolean isSun() {
        return isSun;
    }

    public void setSun(boolean sun) {
        isSun = sun;
    }

    public boolean isMon() {
        return isMon;
    }

    public void setMon(boolean mon) {
        isMon = mon;
    }

    public boolean isTue() {
        return isTue;
    }

    public void setTue(boolean tue) {
        isTue = tue;
    }

    public boolean isWed() {
        return isWed;
    }

    public void setWed(boolean wed) {
        isWed = wed;
    }

    public boolean isThu() {
        return isThu;
    }

    public void setThu(boolean thu) {
        isThu = thu;
    }

    public boolean isFri() {
        return isFri;
    }

    public void setFri(boolean fri) {
        isFri = fri;
    }
}
