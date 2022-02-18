package com.app.medicinealert.uis.activity_base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.medicinealert.language.Language;
import com.app.medicinealert.models.UserModel;
import com.app.medicinealert.preferences.Preferences;

import io.paperdb.Paper;

public class ActivityBase extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected String getLang() {
        Paper.init(this);
        String lang = Paper.book().read("lang", "ar");
        return lang;
    }

    protected UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(this);
    }

    protected void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(this, userModel);
    }

    protected void clearUserModel() {
        Preferences preferences = Preferences.getInstance();
        preferences.clearUserData(this);

    }


}
