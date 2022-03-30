package com.app.medicinealert.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.esotericsoftware.kryo.NotNull;

import java.io.Serializable;

@Entity(tableName = "table_report")
public class ReportModel implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String user_id;
    private String medicine_id;
    private String medicine_name;
    private int dosage;
    private int total_amount;
    private String date;

    public ReportModel(String user_id, String medicine_id, String medicine_name, int dosage, int total_amount,String date) {
        this.user_id = user_id;
        this.medicine_id = medicine_id;
        this.medicine_name = medicine_name;
        this.dosage = dosage;
        this.total_amount = total_amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
