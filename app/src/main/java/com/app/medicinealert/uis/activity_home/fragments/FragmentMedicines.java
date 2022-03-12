package com.app.medicinealert.uis.activity_home.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.medicinealert.R;
import com.app.medicinealert.adapters.MedicinesAdapter;
import com.app.medicinealert.databinding.FragmentHomeBinding;
import com.app.medicinealert.databinding.FragmentMedicinesBinding;
import com.app.medicinealert.models.MedicineModel;
import com.app.medicinealert.tags.Common;
import com.app.medicinealert.tags.Tags;
import com.app.medicinealert.uis.activity_base.FragmentBase;
import com.app.medicinealert.uis.activity_home.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentMedicines extends FragmentBase {
    private FragmentMedicinesBinding binding;
    private HomeActivity activity;
    private MedicinesAdapter adapter;
    private List<MedicineModel> list;
    private DatabaseReference dRef;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_medicines, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        list = new ArrayList<>();
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new MedicinesAdapter(activity, this);
        adapter.updateList(list);
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getMedicines);
        binding.fab.setOnClickListener(view -> {
            activity.showAddMedicineDialog(null);
        });
        getMedicines();
    }

    private void getMedicines() {

        binding.tvNoData.setVisibility(View.GONE);
        binding.swipeRefresh.setRefreshing(true);
        dRef = FirebaseDatabase.getInstance().getReference();
        Query dRefSync = dRef.child(Tags.table_medicines).orderByChild("user_id")
                .equalTo(getUserModel().getUser_id());

        dRefSync.keepSynced(true);
        dRefSync.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.swipeRefresh.setRefreshing(false);
                list.clear();
                adapter.updateList(list);

                if (snapshot.getValue() != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MedicineModel model = ds.getValue(MedicineModel.class);

                        if (model != null) {
                            list.add(model);
                        }
                    }
                    if (list.size() == 0) {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvNoData.setVisibility(View.GONE);
                    }
                    adapter.updateList(list);

                } else {
                    list.clear();
                    adapter.updateList(list);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void edit(MedicineModel model) {
        activity.showAddMedicineDialog(model);
    }

    public void delete(MedicineModel model) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        dRef.child(Tags.table_medicines)
                .child(model.getId())
                .removeValue()
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Common.createAlertDialog(activity, e.getMessage());
        });
    }
}