package com.app.medicinealert.uis.activity_home.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.medicinealert.R;
import com.app.medicinealert.adapters.MedicinesAdapter;
import com.app.medicinealert.adapters.ReportAdapter;
import com.app.medicinealert.databinding.FragmentMedicinesBinding;
import com.app.medicinealert.databinding.FragmentReportsBinding;
import com.app.medicinealert.models.MedicineModel;
import com.app.medicinealert.models.ReportModel;
import com.app.medicinealert.mvvm.AlarmMvvm;
import com.app.medicinealert.mvvm.ReportMvvm;
import com.app.medicinealert.tags.Common;
import com.app.medicinealert.tags.Tags;
import com.app.medicinealert.uis.activity_base.FragmentBase;
import com.app.medicinealert.uis.activity_home.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentReports extends FragmentBase {
    private FragmentReportsBinding binding;
    private HomeActivity activity;
    private ReportAdapter adapter;
    private ReportMvvm mvvm;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reports, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mvvm = ViewModelProviders.of(activity).get(ReportMvvm.class);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ReportAdapter(activity, this);
        binding.recView.setAdapter(adapter);
        mvvm.getOnDataSuccess().observe(activity, list -> {
            if (list != null && list.size() > 0) {
                binding.tvNoData.setVisibility(View.GONE);
            } else {
                binding.tvNoData.setVisibility(View.VISIBLE);
            }
            if (adapter != null) {
                adapter.updateList(list);
            }
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            mvvm.getReportData(getUserModel().getUser_id(), activity);

        });
        mvvm.getReportData(getUserModel().getUser_id(), activity);

    }


}