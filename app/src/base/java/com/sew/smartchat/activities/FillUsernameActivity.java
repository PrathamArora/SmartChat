package com.sew.smartchat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.sew.smartchat.R;

import java.util.Objects;

public class FillUsernameActivity extends BaseActivity {
    TextInputEditText username;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_username);
        final SharedPreferences sharedPreferences = getSharedPreferences(USER_DETAILS, MODE_PRIVATE);

        if (sharedPreferences.contains(USER_NAME) && sharedPreferences.getString(USER_NAME, "").length() != 0) {
            Intent intent = new Intent(FillUsernameActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
        }

        username = findViewById(R.id.username);
        nextButton = findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(username.getText()).toString().trim().length() == 0) {
                    showToast(getString(R.string.please_enter_username));
                    return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_NAME, username.getText().toString());
                editor.apply();

                Intent intent = new Intent(FillUsernameActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
