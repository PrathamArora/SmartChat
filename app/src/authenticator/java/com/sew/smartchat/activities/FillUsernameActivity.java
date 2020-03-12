package com.sew.smartchat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sew.smartchat.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FillUsernameActivity extends BaseActivity {
    TextInputEditText username, password, etPhone, etOtp;
    Button nextButton, btnVerifyPhone;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_username);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(FillUsernameActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
        }

//        final SharedPreferences sharedPreferences = getSharedPreferences(USER_DETAILS, MODE_PRIVATE);

//        if (sharedPreferences.contains(USER_NAME) && sharedPreferences.getString(USER_NAME, "").length() != 0) {
//            Intent intent = new Intent(FillUsernameActivity.this, ChatActivity.class);
//            startActivity(intent);
//            finish();
//        }

        username = findViewById(R.id.username);
        nextButton = findViewById(R.id.next_button);
        password = findViewById(R.id.password);
        btnVerifyPhone = findViewById(R.id.btnVerifyPhone);
        etPhone = findViewById(R.id.etPhone);
        etOtp = findViewById(R.id.etOtp);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(username.getText()).toString().trim().length() == 0) {
                    showToast(getString(R.string.please_enter_username));
                    return;
                }

                if (Objects.requireNonNull(password.getText()).toString().trim().length() == 0) {
                    showToast(getString(R.string.please_enter_password));
                    return;
                }

                String strUsername = username.getText().toString().trim();
                String strPassword = password.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(strUsername + "@something.com", strPassword)
                        .addOnCompleteListener(FillUsernameActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    showToast("Authentication Failed");
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        btnVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPhoneNumber = Objects.requireNonNull(etPhone.getText()).toString().trim();
                String strOTP = Objects.requireNonNull(etOtp.getText()).toString().trim();

                if (strPhoneNumber.length() != 10) {
                    showToast("Enter a Valid Phone Number");
                }else if(etPhone.isEnabled() || etPhone.isFocusable()){
                    etPhone.setEnabled(false);
                    etPhone.setFocusable(false);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + strPhoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            TaskExecutors.MAIN_THREAD,
                            mCallbacks);
                }else if(strOTP.length() != 0){
                    verifyVerificationCode(strOTP);
                }
            }
        });
    }

    private String mVerificationId = "";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                etOtp.setText(code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            showToast(e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            showToast("Code Sent");
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        signInWithPhone(credential);
    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(FillUsernameActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(USER_DETAILS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(USER_NAME, Objects.requireNonNull(etPhone.getText()).toString().trim());
                            editor.apply();

                            Intent intent = new Intent(FillUsernameActivity.this, ChatActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            SharedPreferences sharedPreferences = getSharedPreferences(USER_DETAILS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_NAME, user.getEmail());
            editor.apply();

            Intent intent = new Intent(FillUsernameActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();

        }
    }
}
