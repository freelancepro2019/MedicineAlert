package com.app.medicinealert.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class AlarmModel implements Serializable {
    @PrimaryKey
    private long alarm_id;
    private String medicine_id;
    private String user_id;
    private String medicine_name;
    private String medicine_dosage;
    private String time;
    private String note;
    private boolean isSat;
    private boolean isSun;
    private boolean isMon;
    private boolean isTue;
    private boolean isWed;
    private boolean isThu;
    private boolean isFri;

    public AlarmModel() {
    }

    public AlarmModel(String medicine_id, String user_id, String medicine_name, String medicine_dosage, String time, String note, boolean isSat, boolean isSun, boolean isMon, boolean isTue, boolean isWed, boolean isThu, boolean isFri) {
        this.medicine_id = medicine_id;
        this.user_id = user_id;
        this.medicine_name = medicine_name;
        this.medicine_dosage = medicine_dosage;
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

    public long getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(long alarm_id) {
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
