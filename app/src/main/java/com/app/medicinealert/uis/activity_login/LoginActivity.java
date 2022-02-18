package com.app.medicinealert.uis.activity_login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.ActivityLoginBinding;
import com.app.medicinealert.models.LoginModel;
import com.app.medicinealert.models.UserModel;
import com.app.medicinealert.tags.Common;
import com.app.medicinealert.tags.Tags;
import com.app.medicinealert.uis.activity_base.ActivityBase;
import com.app.medicinealert.uis.activity_home.HomeActivity;
import com.app.medicinealert.uis.activity_sign_up.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends ActivityBase {
    private ActivityLoginBinding binding;
    private LoginModel model;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView() {
        model = new LoginModel();
        binding.setModel(model);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK) {
                navigateToHomeActivity();
            }
        });
        binding.btnLogin.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                Common.CloseKeyBoard(this,binding.edtEmail);
                login();
            }
        });

        binding.llSignUp.setOnClickListener(view -> {
            navigateToSignUpActivity();
        });
    }


    private void login() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.logging_in));
        dialog.setCancelable(false);
        dialog.show();
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(model.getEmail(), model.getPassword())
                .addOnSuccessListener(authResult -> {
                    if (authResult != null && authResult.getUser() != null) {
                        getUserById(authResult.getUser().getUid(), dialog);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(e -> {
                    if (e.getMessage()!=null&&e.getMessage().contains("no user record")){
                        Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                    }else {
                        Common.createAlertDialog(LoginActivity.this, e.getMessage());

                    }

            dialog.dismiss();
        });
    }

    private void getUserById(String user_id, ProgressDialog dialog) {
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_patients);
        dRef.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismiss();
                if (snapshot.getValue() != null) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        setUserModel(userModel);
                        navigateToHomeActivity();
                    } else {
                        Common.createAlertDialog(LoginActivity.this, getString(R.string.error_msg));
                    }
                } else {
                    Toast.makeText(LoginActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("error_login", error.getMessage());
            }
        });
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSignUpActivity() {
        req = 1;
        Intent intent = new Intent(this, SignUpActivity.class);
        launcher.launch(intent);
    }
}