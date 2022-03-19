package com.app.medicinealert.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.SpinnerRowBinding;
import com.app.medicinealert.models.MedicineModel;

import java.util.List;

public class SpinnerMedicineAdapter extends BaseAdapter {
    private List<MedicineModel> list;
    private Context context;

    public SpinnerMedicineAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int i) {
        return list!=null?list.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_row,viewGroup,false);
        binding.setTitle(list.get(i).getName());
        return binding.getRoot();
    }

    public void updateList(List<MedicineModel> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
