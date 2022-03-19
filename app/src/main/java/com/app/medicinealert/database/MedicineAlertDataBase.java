package com.app.medicinealert.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app.medicinealert.models.AlarmModel;


@Database(entities = {AlarmModel.class},version = 1,exportSchema = false)
public abstract class MedicineAlertDataBase extends RoomDatabase {

    private static MedicineAlertDataBase instance = null;
    public abstract DAO getDAO();

    public static synchronized MedicineAlertDataBase newInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),MedicineAlertDataBase.class,"medicine_alarm_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
