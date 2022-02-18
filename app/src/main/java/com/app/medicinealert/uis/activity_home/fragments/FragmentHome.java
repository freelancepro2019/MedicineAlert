package com.app.medicinealert.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.FragmentHomeBinding;
import com.app.medicinealert.uis.activity_base.FragmentBase;
import com.app.medicinealert.uis.activity_home.HomeActivity;

public class FragmentHome extends FragmentBase {
    private HomeActivity activity;
    private FragmentHomeBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.fab.setOnClickListener(view -> {
            activity.showAlarmDialog(null);
        });
    }
}