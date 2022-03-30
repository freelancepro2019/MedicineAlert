package com.app.medicinealert.uis.activity_ringtone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.app.medicinealert.R;
import com.app.medicinealert.alarm_broadcasat.AlarmReceiver;
import com.app.medicinealert.database.DAO;
import com.app.medicinealert.database.MedicineAlertDataBase;
import com.app.medicinealert.databinding.ActivityRingtoneBinding;
import com.app.medicinealert.models.ReportModel;
import com.app.medicinealert.services.AlarmService;
import com.app.medicinealert.uis.activity_base.ActivityBase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RingtoneActivity extends ActivityBase {
    private ActivityRingtoneBinding binding;
    public final String TAG = this.getClass().getSimpleName();
    private PowerManager.WakeLock mWakeLock;
    private MediaPlayer mp;
    private int alarm_id;
    private String title;
    private String dosage;
    private String note;
    private String medicine_id;
    private MedicineAlertDataBase dataBase;
    private DAO dao;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ringtone);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Log.e("ringtone","ringtone");
        Intent intent = getIntent();
        alarm_id = intent.getIntExtra(AlarmReceiver.ALARM_ID, 0);
        title = intent.getStringExtra(AlarmReceiver.TITLE);
        dosage = intent.getStringExtra(AlarmReceiver.DOSAGE);
        note = intent.getStringExtra(AlarmReceiver.NOTE);
        medicine_id = intent.getStringExtra(AlarmReceiver.MEDICINE_ID);
    }

    private void initView() {
        dataBase = MedicineAlertDataBase.newInstance(this);
        dao = dataBase.getDAO();

       /* PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }

        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire(60 * 1000L);
        }
*/
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
        if (uri != null) {
            mp = MediaPlayer.create(this, uri);
            mp.setLooping(true);
            mp.setVolume(1.0f, 1.0f);
            mp.start();

        }

        binding.btnDone.setOnClickListener(view -> {
            insertReport();
        });
        animateAlarm();

    }

    private void insertReport() {
        ReportModel model = new ReportModel(getUserModel().getUser_id(),medicine_id,title,Integer.parseInt(dosage),0,getNowDate());
        dao.insertReport(model)
                .subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                        getApplicationContext().stopService(intentService);
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void animateAlarm() {
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(binding.imageAlarm, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        if (mp != null) {
            mp.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
        }
        disposable.clear();
    }

    private String getNowDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH);
        return simpleDateFormat.format(calendar.getTime());
    }
}