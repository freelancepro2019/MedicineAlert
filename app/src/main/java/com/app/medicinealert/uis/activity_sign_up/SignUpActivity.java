package com.app.medicinealert.uis.activity_sign_up;

import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.ActivitySignUpBinding;
import com.app.medicinealert.models.SignUpModel;
import com.app.medicinealert.models.UserModel;
import com.app.medicinealert.tags.Common;
import com.app.medicinealert.tags.Tags;
import com.app.medicinealert.uis.activity_base.ActivityBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends ActivityBase {
    private ActivitySignUpBinding binding;
    private SignUpModel model;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        model = new SignUpModel();
        binding.setModel(model);

        binding.llLogin.setOnClickListener(view -> {
            navigateToLoginActivity();
        });

        binding.btnSignUp.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.logging_in));
        dialog.setCancelable(false);
        dialog.show();
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(model.getEmail(), model.getPassword())
                .addOnSuccessListener(authResult -> {
                    if (authResult != null && authResult.getUser() != null) {
                        signUp(authResult.getUser().getUid(), dialog);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(e -> {
            Common.createAlertDialog(SignUpActivity.this, e.getMessage());

            dialog.dismiss();
        });
    }

    private void signUp(String user_id, ProgressDialog dialog) {
        UserModel userModel = new UserModel(user_id, model.getFirstName(), model.getLastName(), model.getPhoneCode(), model.getPhone(), model.getEmail(), model.getPassword());
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_patients)
                .child(user_id)
                .setValue(userModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    setUserModel(userModel);
                    setResult(RESULT_OK);
                    finish();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Common.createAlertDialog(this, e.getMessage());
        });
    }

    private void navigateToLoginActivity() {
        finish();
    }
}