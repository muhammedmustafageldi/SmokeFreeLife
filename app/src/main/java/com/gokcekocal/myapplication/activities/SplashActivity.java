package com.gokcekocal.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.gokcekocal.myapplication.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);


        new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent intent;
                if (isLoggedIn) {
                    // If previously logged in go to main activity.
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    // If not previously logged in go to welcome activity.
                    intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                }
                binding.lottieAnimationSplash.cancelAnimation();
                startActivity(intent);
            }
        }.start();

    }
}