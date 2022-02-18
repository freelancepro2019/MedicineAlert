package com.app.medicinealert.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.app.medicinealert.BR;
import com.app.medicinealert.R;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

public class MedicineModel extends BaseObservable implements Serializable {
    private String id="";
    private String user_id;
    private String name = "";
    private String dosage = "";
    @Exclude
    public ObservableField<String> error_name = new ObservableField<>();
    @Exclude
    public ObservableField<String> error_dosage = new ObservableField<>();

    public MedicineModel() {

    }

    public MedicineModel(String id, String user_id, String name, String dosage) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.dosage = dosage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
        notifyPropertyChanged(BR.dosage);
    }

    public boolean isDataValid(Context context) {
        if (!name.isEmpty() && !dosage.isEmpty()) {

            error_name.set(null);
            error_dosage.set(null);
            return true;
        } else {
            if (name.isEmpty()){
                error_name.set(context.getString(R.string.field_required));
            }else {
                error_name.set(null);

            }

            if (dosage.isEmpty()){
                error_dosage.set(context.getString(R.string.field_required));

            }else {
                error_dosage.set(null);

            }
            return false;
        }
    }
}
