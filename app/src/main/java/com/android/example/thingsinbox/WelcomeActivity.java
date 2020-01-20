package com.android.example.thingsinbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mLists;
    private EditText mTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mEmail = findViewById(R.id.et_welcome_email);
        mLists = findViewById(R.id.et_welcome_lists);
        mTags = findViewById(R.id.et_welcome_tags);
    }

    public void onSave(View v) {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(this);
        manager.edit()
                .putString("email", mEmail.getText().toString())
                .putString("lists", mLists.getText().toString())
                .putString("tags", mTags.getText().toString())
                .apply();
        finish();
    }

    public void skipWelcome(View v) {
        finish();
    }
}