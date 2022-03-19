package com.app.medicinealert.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.medicinealert.R;
import com.app.medicinealert.adapters.AlarmAdapter;
import com.app.medicinealert.databinding.FragmentHomeBinding;
import com.app.medicinealert.models.AlarmModel;
import com.app.medicinealert.mvvm.AlarmMvvm;
import com.app.medicinealert.uis.activity_base.FragmentBase;
import com.app.medicinealert.uis.activity_home.HomeActivity;

public class FragmentHome extends FragmentBase {
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private AlarmMvvm mvvm;
    private AlarmAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mvvm = ViewModelProviders.of(activity).get(AlarmMvvm.class);
        adapter = new AlarmAdapter(activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        mvvm.getOnDataSuccess().observe(activity, list -> {
            adapter.updateList(list);

        });
        mvvm.getIsDataChanged().observe(activity, list -> {
            adapter.notifyDataSetChanged();

        });

        mvvm.onRefresh().observe(activity, isRefreshed -> {
            if (isRefreshed) {
                mvvm.getAlarmData(getUserModel().getUser_id(), activity);
            }
        });

        mvvm.getOnDeleteSuccess().observe(activity, pos -> {
            adapter.notifyItemRemoved(pos);
        });
        mvvm.getIsDataChanged().observe(activity, pos -> {
            adapter.notifyItemChanged(pos);
        });

        mvvm.getAlarmData(getUserModel().getUser_id(), activity);


        binding.fab.setOnClickListener(view -> {
            activity.showAlarmDialog(null, -1);
        });
    }

    public void update(AlarmModel model, int adapterPosition) {
        activity.showAlarmDialog(model, adapterPosition);
    }

    public void cancel(AlarmModel model, int adapterPosition) {
        if (model.isRecurring()){
            model.cancelAlarm(activity);
            mvvm.update(model, activity, adapterPosition);
        }else {
            mvvm.delete(model.getAlarm_id(),model.getUser_id(),activity);
        }

    }
}