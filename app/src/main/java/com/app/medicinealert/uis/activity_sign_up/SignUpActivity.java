package com.app.medicinealert.uis.activity_sign_up;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.medicinealert.R;
import com.app.medicinealert.databinding.ActivitySignUpBinding;
import com.app.medicinealert.models.SignUpModel;
import com.app.medicinealert.models.UserModel;
import com.app.medicinealert.tags.Common;
import com.app.medicinealert.tags.Tags;
import com.app.medicinealert.uis.activity_base.ActivityBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
        if (getUserModel() != null) {

            model.setFirstName(getUserModel().getFirst_name());
            model.setLastName(getUserModel().getLast_name());
            model.setEmail(getUserModel().getEmail());
            model.setPhoneCode(getUserModel().getPhone_code());
            model.setPhone(getUserModel().getPhone());
            model.setPassword(getUserModel().getPassword());
            binding.tv.setText(getString(R.string.update_profile));
            binding.btnSignUp.setText(getText(R.string.update_profile));
            binding.llLogin.setVisibility(View.GONE);

        }
        binding.setModel(model);

        binding.llLogin.setOnClickListener(view -> {
            navigateToLoginActivity();
        });

        binding.btnSignUp.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                if (getUserModel() == null) {
                    createAccount();

                } else {
                    reAuth();
                }
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

    private void reAuth() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.logging_in));
        dialog.setCancelable(false);
        dialog.show();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(getUserModel().getEmail(), getUserModel().getPassword());
            currentUser.reauthenticate(credential).addOnSuccessListener(unused -> {


                currentUser.updateEmail(model.getEmail())
                        .addOnSuccessListener(unused1 -> {
                            currentUser.updatePassword(model.getPassword())
                                    .addOnSuccessListener(unused2 -> {
                                        updateProfile(dialog);
                                    }).addOnFailureListener(e -> {
                                dialog.dismiss();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            });
                        }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                });

            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Session expired login again", Toast.LENGTH_SHORT).show();

        }

    }

    private void updateProfile(ProgressDialog dialog) {
        signUp(getUserModel().getUser_id(),dialog);

    }

    private void navigateToLoginActivity() {
        finish();
    }
}