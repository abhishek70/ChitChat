package com.example.abhishek.chitchat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialized the intent and start the Movies Activity
        Intent intent = new Intent(this, ChitChatActivity.class);
        startActivity(intent);
        finish();
    }
}
