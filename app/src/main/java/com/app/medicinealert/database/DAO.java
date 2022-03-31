package com.app.medicinealert.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.medicinealert.models.AlarmModel;
import com.app.medicinealert.models.ReportModel;
import com.app.medicinealert.models.UserModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(AlarmModel model);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable update(AlarmModel model);

    @Query("DELETE FROM alarm_table WHERE alarm_id =:alarm_id")
    Completable delete(int alarm_id);

    @Query("SELECT * FROM alarm_table WHERE user_id=:user_id ORDER BY alarm_id ASC")
    Single<List<AlarmModel>> getMyAlarm(String user_id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertReport(ReportModel model);

    @Query("SELECT * ,COUNT(*) AS total_amount FROM table_report WHERE user_id=:user_id GROUP BY dosage")
    Single<List<ReportModel>> getReports(String user_id);

}
