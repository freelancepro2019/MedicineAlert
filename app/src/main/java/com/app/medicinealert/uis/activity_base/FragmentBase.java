package com.app.medicinealert.uis.activity_base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.medicinealert.models.UserModel;
import com.app.medicinealert.preferences.Preferences;

import io.paperdb.Paper;

public class FragmentBase extends Fragment {
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    protected String getLang() {
        Paper.init(context);
        String lang = Paper.book().read("lang", "ar");
        return lang;
    }

    protected UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(context);

    }

    protected void clearUserModel(Context context) {
        Preferences preferences = Preferences.getInstance();
        preferences.clearUserData(context);

    }

    protected void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(context, userModel);
    }


}
