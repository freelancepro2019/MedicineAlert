package com.app.medicinealert.uis.activity_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.FragmentHomeBinding;
import com.app.medicinealert.databinding.FragmentProfileBinding;
import com.app.medicinealert.uis.activity_base.FragmentBase;
import com.app.medicinealert.uis.activity_home.HomeActivity;
import com.app.medicinealert.uis.activity_sign_up.SignUpActivity;

public class FragmentProfile extends FragmentBase {
    private FragmentProfileBinding binding;
    private HomeActivity activity;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                binding.setModel(getUserModel());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.setLang(getLang());
        binding.setModel(getUserModel());
        binding.cardLogout.setOnClickListener(view -> {
            activity.logout();
        });

        binding.llAddMedicine.setOnClickListener(view -> {
            activity.showAddMedicineDialog(null);
        });

        binding.llMedicine.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.fragmentMedicine);
        });

        binding.llReports.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.fragmentReports);
        });

        binding.llEditProfile.setOnClickListener(view -> {
            req = 1;
            Intent intent = new Intent(activity, SignUpActivity.class);
            launcher.launch(intent);
        });
    }
}