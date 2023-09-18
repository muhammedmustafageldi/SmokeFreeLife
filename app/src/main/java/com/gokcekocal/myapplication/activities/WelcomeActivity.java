package com.gokcekocal.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.gokcekocal.myapplication.databinding.ActivityWelcomeBinding;
import com.gokcekocal.myapplication.models.UserDetails;
import com.gokcekocal.myapplication.roomdb.MyAppDatabase;
import com.gokcekocal.myapplication.roomdb.MyDao;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.welcomeFabButton.setOnClickListener(view -> {
            if (validator()) {
                binding.welcomeFabButton.setEnabled(false);
                TextInputEditText cigarettesPerDayTxt = binding.cigarettesPerDayTxt;
                TextInputEditText cigarettesPerPackTxt = binding.cigarettesPerPackTxt;
                TextInputEditText yearsOfSmokingTxt = binding.yearsOfSmokingTxt;
                TextInputEditText pricePerPackageTxt = binding.pricePerPackageTxt;

                int cigarettesPerDay = Integer.parseInt(Objects.requireNonNull(cigarettesPerDayTxt.getText()).toString().trim());
                int cigarettesPerPack = Integer.parseInt(Objects.requireNonNull(cigarettesPerPackTxt.getText()).toString().trim());
                int yearsOfSmoking = Integer.parseInt(Objects.requireNonNull(yearsOfSmokingTxt.getText()).toString().trim());
                float pricePerPackage = Float.parseFloat(Objects.requireNonNull(pricePerPackageTxt.getText()).toString().trim());

                save(cigarettesPerDay, cigarettesPerPack, yearsOfSmoking, pricePerPackage);

            } else {
                Toast.makeText(this, "Geçersiz değerler.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validator() {
        boolean isValid = true;

        TextInputEditText cigarettesPerDayTxt = binding.cigarettesPerDayTxt;
        TextInputEditText cigarettesPerPackTxt = binding.cigarettesPerPackTxt;
        TextInputEditText yearsOfSmokingTxt = binding.yearsOfSmokingTxt;
        TextInputEditText pricePerPackageTxt = binding.pricePerPackageTxt;

        String value1 = Objects.requireNonNull(cigarettesPerDayTxt.getText()).toString().trim();
        if (value1.isEmpty() || value1.equals("0") || value1.equals("00")) {
            cigarettesPerDayTxt.setError("Bu alanı doldurun ve 0'dan farklı bir değer girin");
            isValid = false;
        }

        String value2 = Objects.requireNonNull(cigarettesPerPackTxt.getText()).toString().trim();
        if (value2.isEmpty() || value2.equals("0") || value2.equals("00")) {
            cigarettesPerPackTxt.setError("Bu alanı doldurun ve 0'dan farklı bir değer girin");
            isValid = false;
        }

        String value3 = Objects.requireNonNull(yearsOfSmokingTxt.getText()).toString().trim();
        if (value3.isEmpty() || value3.equals("0") || value3.equals("00")) {
            yearsOfSmokingTxt.setError("Bu alanı doldurun ve 0'dan farklı bir değer girin");
            isValid = false;
        }

        String value4 = Objects.requireNonNull(pricePerPackageTxt.getText()).toString().trim();
        if (value4.isEmpty() || value4.equals("0") || value4.equals("00") || value4.equals("000")) {
            pricePerPackageTxt.setError("Bu alanı doldurun ve 0'dan farklı bir değer girin");
            isValid = false;
        }

        return isValid;
    }

    private void save(int cigarettesPerDay, int cigarettesPerPack, int yearsOfSmoking, float pricePerPackage) {
        MyAppDatabase database = MyAppDatabase.getInstance(getApplicationContext());
        MyDao dao = database.myDao();
        compositeDisposable = new CompositeDisposable();

        // Save the user details
        compositeDisposable.add(dao.insertUser(new UserDetails(1, cigarettesPerDay, cigarettesPerPack, yearsOfSmoking, pricePerPackage))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(WelcomeActivity.this::saveCompleted));
    }

    private void saveCompleted() {
        //Take the current time in seconds
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("userTime", currentTimeInSeconds);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}