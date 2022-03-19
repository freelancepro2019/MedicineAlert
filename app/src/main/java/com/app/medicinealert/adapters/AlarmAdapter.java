
package com.app.medicinealert.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.AlarmRowBinding;
import com.app.medicinealert.databinding.MedicineRowBinding;
import com.app.medicinealert.models.AlarmModel;
import com.app.medicinealert.models.MedicineModel;
import com.app.medicinealert.uis.activity_home.fragments.FragmentHome;
import com.app.medicinealert.uis.activity_home.fragments.FragmentMedicines;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AlarmModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    public AlarmAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        AlarmRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.alarm_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentHome) {
                FragmentHome fragmentHome = (FragmentHome) fragment;
                fragmentHome.update(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());
            }
        });

        myHolder.binding.switchBtn.setOnClickListener(view -> {
            if (fragment instanceof FragmentHome) {
                boolean isChecked = myHolder.binding.switchBtn.isChecked();
                AlarmModel alarmModel = list.get(myHolder.getAdapterPosition());
                alarmModel.setStarted(isChecked);

                FragmentHome fragmentHome = (FragmentHome) fragment;
                fragmentHome.cancel(list.get(myHolder.getAdapterPosition()), myHolder.getAdapterPosition());
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
        public AlarmRowBinding binding;

        public MyHolder(AlarmRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<AlarmModel> list) {
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
