
package com.app.medicinealert.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.MedicineRowBinding;
import com.app.medicinealert.models.MedicineModel;
import com.app.medicinealert.uis.activity_home.fragments.FragmentMedicines;

import java.util.List;

public class MedicinesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MedicineModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    public MedicinesAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        MedicineRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.medicine_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.edit.setOnClickListener(v -> {
            if (fragment instanceof FragmentMedicines){
                FragmentMedicines fragmentMedicines = (FragmentMedicines) fragment;
                fragmentMedicines.edit(list.get(myHolder.getAdapterPosition()));
            }
        });


        myHolder.binding.delete.setOnClickListener(v -> {
            if (fragment instanceof FragmentMedicines){
                FragmentMedicines fragmentMedicines = (FragmentMedicines) fragment;
                fragmentMedicines.delete(list.get(myHolder.getAdapterPosition()));
            }
        });


    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public MedicineRowBinding binding;

        public MyHolder(MedicineRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<MedicineModel> list) {
        if (list == null) {
            if (this.list != null) {
                this.list.clear();

            }

        } else {
            this.list = list;

        }
        notifyDataSetChanged();
    }

}
