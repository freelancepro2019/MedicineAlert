package com.app.medicinealert.uis.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.ActivityHomeBinding;
import com.app.medicinealert.models.AlarmModel;
import com.app.medicinealert.models.MedicineModel;
import com.app.medicinealert.models.UserModel;
import com.app.medicinealert.tags.Common;
import com.app.medicinealert.tags.Tags;
import com.app.medicinealert.uis.activity_base.ActivityBase;
import com.app.medicinealert.uis.activity_login.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends ActivityBase {
    private ActivityHomeBinding binding;
    private NavController navController;
    private BottomSheetBehavior behavior,behaviorAlarm;
    private DatabaseReference dRef;
    private MedicineModel model;
    private AlarmModel alarmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        initView();
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.toolbar,navController);
        behavior = BottomSheetBehavior.from(binding.sheet.nestedScroll);
        behaviorAlarm = BottomSheetBehavior.from(binding.sheetAlarm.nestedScrollAlarm);

        binding.sheet.close.setOnClickListener(view -> {
            hideAddMedicineDialog();
        });

        binding.sheetAlarm.close.setOnClickListener(view -> {
            hideAlarmDialog();
        });

        binding.sheet.btnAdd.setOnClickListener(view -> {
            if (model.isDataValid(this)){
                add_update_medicine(model);

            }
        });
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            int id = navDestination.getId();
            if (id==R.id.fragmentHome){
                binding.setTitle(getString(R.string.alerts));
            }else if (id==R.id.fragmentProfile){
                binding.setTitle(getString(R.string.profile));

            }else if (id==R.id.fragmentMedicine){
                binding.setTitle(getString(R.string.medicines));

            }
        });
    }

    public void logout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
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
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.addMedicine){
            showAddMedicineDialog(null);

            return true;
        }
        return NavigationUI.onNavDestinationSelected(item,navController)||super.onOptionsItemSelected(item);
    }

    public void showAddMedicineDialog(MedicineModel model) {
        this.model = model;
        if (model==null){
            this.model = new MedicineModel();
        }else {
            binding.sheet.tvTitle.setText(R.string.update_medicine);
            binding.sheet.btnAdd.setText(R.string.update_medicine);
        }
        binding.sheet.setModel(this.model);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void hideAddMedicineDialog() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    public void showAlarmDialog(AlarmModel model) {

        binding.sheet.setModel(this.model);
        behaviorAlarm.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void hideAlarmDialog() {
        behaviorAlarm.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private void add_update_medicine(MedicineModel model) {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();

        String id = "";
        if (model.getId().isEmpty()){
            id =  dRef.child(Tags.table_medicines).push().getKey();
        }else {
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
            Common.createAlertDialog(this,e.getMessage());
        });

    }

}