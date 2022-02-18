package com.app.medicinealert.tags;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.DialogErrorBinding;

public class Common {


    public static void CloseKeyBoard(Context context, View view) {
        if (context != null && view != null) {
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }

        }


    }

    public static void createAlertDialog(Context context, String msg) {
        AlertDialog builder = new AlertDialog.Builder(context).create();
        DialogErrorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.dialog_error,null,false);
        binding.setMessage(msg);
        builder.setView(binding.getRoot());
        binding.btnCancel.setOnClickListener(view -> {
            builder.dismiss();
        });
        builder.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        builder.show();
    }
    public static ProgressDialog createProgressDialog(Context context, String msg) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        ProgressBar bar = new ProgressBar(context);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        dialog.setIndeterminateDrawable(drawable);
        return dialog;

    }


}
