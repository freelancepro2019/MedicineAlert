package com.app.medicinealert.uis.activity_home;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.medicinealert.R;
import com.app.medicinealert.adapters.SpinnerMedicineAdapter;
import com.app.medicinealert.databinding.ActivityHomeBinding;
import com.app.medicinealert.models.AlarmModel;
import com.app.medicinealert.models.MedicineModel;
import com.app.medicinealert.models.UserModel;
import com.app.medicinealert.mvvm.AlarmMvvm;
import com.app.medicinealert.tags.Common;
import com.app.medicinealert.tags.Tags;
import com.app.medicinealert.uis.activity_base.ActivityBase;
import com.app.medicinealert.uis.activity_login.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class HomeActivity extends ActivityBase implements TimePickerDialog.OnTimeSetListener {
    private ActivityHomeBinding binding;
    private NavController navController;
    private BottomSheetBehavior behavior, behaviorAlarm;
    private DatabaseReference dRef;
    private MedicineModel model;
    private List<MedicineModel> list;
    private SpinnerMedicineAdapter adapter;
    private String time = "";
    private int hour;
    private int minute;
    private AlarmMvvm mvvm;
    private AlarmModel alarmModel;
    private int updatePos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(AlarmMvvm.class);

        list = new ArrayList<>();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.toolbar, navController);
        behavior = BottomSheetBehavior.from(binding.sheet.nestedScroll);
        behaviorAlarm = BottomSheetBehavior.from(binding.sheetAlarm.nestedScrollAlarm);

        binding.sheet.close.setOnClickListener(view -> {
            hideAddMedicineDialog();
        });

        binding.sheetAlarm.close.setOnClickListener(view -> {
            hideAlarmDialog();
        });

        binding.sheet.btnAdd.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                add_update_medicine(model);

            }
        });
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            int id = navDestination.getId();
            if (id == R.id.fragmentHome) {
                binding.setTitle(getString(R.string.alerts));
            } else if (id == R.id.fragmentProfile) {
                binding.setTitle(getString(R.string.profile));

            } else if (id == R.id.fragmentMedicine) {
                binding.setTitle(getString(R.string.medicines));

            }
        });
        adapter = new SpinnerMedicineAdapter(this);
        binding.sheetAlarm.spinner.setAdapter(adapter);

        binding.sheetAlarm.cbRecurring.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                binding.sheetAlarm.llDays.setVisibility(View.VISIBLE);
            } else {
                if (alarmModel == null) {
                    binding.sheetAlarm.cbSat.setChecked(false);
                    binding.sheetAlarm.cbSun.setChecked(false);
                    binding.sheetAlarm.cbMon.setChecked(false);
                    binding.sheetAlarm.cbTus.setChecked(false);
                    binding.sheetAlarm.cbWed.setChecked(false);
                    binding.sheetAlarm.cbThu.setChecked(false);
                    binding.sheetAlarm.cbFri.setChecked(false);
                }

                binding.sheetAlarm.llDays.setVisibility(View.GONE);

            }
        });
        binding.sheetAlarm.btnAdd.setOnClickListener(view -> {
            checkData();
        });
        binding.sheetAlarm.llTime.setOnClickListener(view -> createTimeDialog());
        getMedicines();

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void checkData() {

        MedicineModel model = (MedicineModel) binding.sheetAlarm.spinner.getSelectedItem();
        String note = binding.sheetAlarm.edtNote.getText().toString();
        if (model != null && !time.isEmpty()) {
            if (binding.sheetAlarm.cbRecurring.isChecked()) {
                if (isRecurringAvailable()) {
                    if (alarmModel == null) {
                        addAlarm(model, note);

                    } else {
                        updateAlarm(model, note);
                    }

                } else {
                    Toast.makeText(this, R.string.ch_re_days, Toast.LENGTH_SHORT).show();
                }
            }else {
                if (alarmModel == null) {
                    addAlarm(model, note);

                } else {
                    updateAlarm(model, note);
                }
            }
            binding.sheetAlarm.tvTime.setError(null);

        } else {
            if (time.isEmpty()) {
                binding.sheetAlarm.tvTime.setError(getString(R.string.field_required));
            }

            if (model == null) {
                Toast.makeText(this, R.string.ch_medicine, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void addAlarm(MedicineModel model, String note) {
        Log.e("add","add");
        int alarm_id = new Random().nextInt(Integer.MAX_VALUE);
        AlarmModel alarmModel = new AlarmModel(alarm_id, hour, minute, model.getId(), model.getUser_id(), model.getName(), model.getDosage(), time, note, binding.sheetAlarm.cbRecurring.isChecked(), binding.sheetAlarm.cbSat.isChecked(), binding.sheetAlarm.cbSun.isChecked(), binding.sheetAlarm.cbMon.isChecked(), binding.sheetAlarm.cbTus.isChecked(), binding.sheetAlarm.cbWed.isChecked(), binding.sheetAlarm.cbThu.isChecked(), binding.sheetAlarm.cbFri.isChecked(), true);
        mvvm.insert(alarmModel, this);
        alarmModel.schedule(this);
        this.alarmModel = null;
        updatePos = -1;
        hideAlarmDialog();
    }

    private void updateAlarm(MedicineModel model, String note) {
        alarmModel.setHour(hour);
        alarmModel.setMinute(minute);
        alarmModel.setTime(time);
        alarmModel.setMedicine_id(model.getId());
        alarmModel.setMedicine_name(model.getName());
        alarmModel.setMedicine_dosage(model.getDosage());
        alarmModel.setNote(note);
        mvvm.update(alarmModel, this, 0);
        if (alarmModel.isStarted()) {
            alarmModel.schedule(this);

        } else {
            alarmModel.cancelAlarm(this);
        }

        this.alarmModel = null;
        updatePos = -1;
        hideAlarmDialog();

    }

    private boolean isRecurringAvailable() {
        if (binding.sheetAlarm.cbSat.isChecked()
                || binding.sheetAlarm.cbSun.isChecked()
                || binding.sheetAlarm.cbMon.isChecked()
                || binding.sheetAlarm.cbTus.isChecked()
                || binding.sheetAlarm.cbWed.isChecked()
                || binding.sheetAlarm.cbThu.isChecked()
                || binding.sheetAlarm.cbFri.isChecked()
        ) {
            return true;
        }
        return false;
    }

    private void getMedicines() {

        dRef = FirebaseDatabase.getInstance().getReference();
        Query dRefSync = dRef.child(Tags.table_medicines).orderByChild("user_id")
                .equalTo(getUserModel().getUser_id());

        dRefSync.keepSynced(true);
        dRefSync.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                adapter.updateList(list);

                if (snapshot.getValue() != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MedicineModel model = ds.getValue(MedicineModel.class);

                        if (model != null) {
                            list.add(model);
                        }
                    }

                } else {
                    list.clear();
                }
                adapter.updateList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void logout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            clearUserModel();
            navigateToLoginActivity();
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addMedicine) {
            showAddMedicineDialog(null);

            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    public void showAddMedicineDialog(MedicineModel model) {
        this.model = model;
        if (model == null) {
            this.model = new MedicineModel();
        } else {
            binding.sheet.tvTitle.setText(R.string.update_medicine);
            binding.sheet.btnAdd.setText(R.string.update_medicine);
        }
        binding.sheet.setModel(this.model);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void hideAddMedicineDialog() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    public void showAlarmDialog(AlarmModel model, int updatePos) {
        this.alarmModel = model;
        this.updatePos = updatePos;
        if (model != null) {
            binding.sheetAlarm.tvTime.setText(model.getTime());
            binding.sheetAlarm.edtNote.setText(model.getNote());
            binding.sheetAlarm.cbRecurring.setChecked(model.isRecurring());
            binding.sheetAlarm.cbSat.setChecked(model.isSat());
            binding.sheetAlarm.cbSun.setChecked(model.isSun());
            binding.sheetAlarm.cbMon.setChecked(model.isMon());
            binding.sheetAlarm.cbTus.setChecked(model.isTue());
            binding.sheetAlarm.cbWed.setChecked(model.isWed());
            binding.sheetAlarm.cbThu.setChecked(model.isThu());
            binding.sheetAlarm.cbFri.setChecked(model.isFri());
            binding.sheetAlarm.btnAdd.setText(R.string.update);
        } else {
            binding.sheetAlarm.tvTime.setText("");
            binding.sheetAlarm.edtNote.setText("");
            binding.sheetAlarm.cbRecurring.setChecked(false);
            binding.sheetAlarm.cbSat.setChecked(false);
            binding.sheetAlarm.cbSun.setChecked(false);
            binding.sheetAlarm.cbMon.setChecked(false);
            binding.sheetAlarm.cbTus.setChecked(false);
            binding.sheetAlarm.cbWed.setChecked(false);
            binding.sheetAlarm.cbThu.setChecked(false);
            binding.sheetAlarm.cbFri.setChecked(false);
            binding.sheetAlarm.btnAdd.setText(R.string.add_alarm);
        }
        behaviorAlarm.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void hideAlarmDialog() {
        behaviorAlarm.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private void add_update_medicine(MedicineModel model) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();

        String id = "";
        if (model.getId().isEmpty()) {
            id = dRef.child(Tags.table_medicines).push().getKey();
        } else {
            id = model.getId();
        }
        UserModel userModel = getUserModel();
        model.setId(id);
        model.setUser_id(userModel.getUser_id());
        dRef.child(Tags.table_medicines)
                .child(id)
                .setValue(model)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, R.string.suc, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    hideAddMedicineDialog();
                    this.model = null;
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Common.createAlertDialog(this, e.getMessage());
        });

    }

    private void createTimeDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
        timePickerDialog.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setTitle(getString(R.string.alarm_time));
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setCancelColor(ContextCompat.getColor(this, R.color.gray4));
        timePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setOkColor(ContextCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setOkText(R.string.select);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_1);
        try {
            timePickerDialog.show(getFragmentManager(), "");

        } catch (Exception e) {
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        this.hour = hourOfDay;
        this.minute = minute;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        time = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(calendar.getTime());
        binding.sheetAlarm.tvTime.setText(time);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshDatabase(String refresh){
        mvvm.onRefresh().setValue(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}