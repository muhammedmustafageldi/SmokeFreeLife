package com.gokcekocal.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.gokcekocal.myapplication.databinding.ActivityMyDetailsBinding;
import com.gokcekocal.myapplication.models.UserDetails;
import com.gokcekocal.myapplication.roomdb.MyAppDatabase;
import com.gokcekocal.myapplication.roomdb.MyDao;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyDetailsActivity extends AppCompatActivity {

    private ActivityMyDetailsBinding binding;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getUserData();

        binding.setFabButton.setOnClickListener(view -> {
            if (validator()) {
                binding.setFabButton.setEnabled(false);
                TextInputEditText cigarettesPerDayTxt = binding.cigarettesPerDaySetTxt;
                TextInputEditText cigarettesPerPackTxt = binding.cigarettesPerPackSetTxt;
                TextInputEditText yearsOfSmokingTxt = binding.yearsOfSmokingSetTxt;
                TextInputEditText pricePerPackageTxt = binding.pricePerPackageSetTxt;

                int cigarettesPerDay = Integer.parseInt(Objects.requireNonNull(cigarettesPerDayTxt.getText()).toString().trim());
                int cigarettesPerPack = Integer.parseInt(Objects.requireNonNull(cigarettesPerPackTxt.getText()).toString().trim());
                int yearsOfSmoking = Integer.parseInt(Objects.requireNonNull(yearsOfSmokingTxt.getText()).toString().trim());
                float pricePerPackage = Float.parseFloat(Objects.requireNonNull(pricePerPackageTxt.getText()).toString().trim());

                update(cigarettesPerDay, cigarettesPerPack, yearsOfSmoking, pricePerPackage);

            } else {
                Toast.makeText(this, "Geçersiz değerler.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserData() {
        MyAppDatabase database = MyAppDatabase.getInstance(MyDetailsActivity.this);
        MyDao dao = database.myDao();

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(dao.getAllUserDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MyDetailsActivity.this::showUserData));
    }

    private void showUserData(List<UserDetails> userDetails) {
        UserDetails userDetails1 = userDetails.get(0);
        int cigarettesPerDay = userDetails1.getCigarettesPerDay();
        int cigarettesPerPack = userDetails1.getCigarettesPerPack();
        int yearsOfSmoking = userDetails1.getYearsOfSmoking();
        float pricePerPackage = userDetails1.getPricePerPackage();

        binding.cigarettesPerDaySetTxt.setText(String.valueOf(cigarettesPerDay));
        binding.cigarettesPerPackSetTxt.setText(String.valueOf(cigarettesPerPack));
        binding.yearsOfSmokingSetTxt.setText(String.valueOf(yearsOfSmoking));
        binding.pricePerPackageSetTxt.setText(String.valueOf(pricePerPackage));
    }

    private boolean validator() {
        boolean isValid = true;

        TextInputEditText cigarettesPerDayTxt = binding.cigarettesPerDaySetTxt;
        TextInputEditText cigarettesPerPackTxt = binding.cigarettesPerPackSetTxt;
        TextInputEditText yearsOfSmokingTxt = binding.yearsOfSmokingSetTxt;
        TextInputEditText pricePerPackageTxt = binding.pricePerPackageSetTxt;

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

    private void update(int cigarettesPerDay, int cigarettesPerPack, int yearsOfSmoking, float pricePerPackage) {
        MyAppDatabase database = MyAppDatabase.getInstance(getApplicationContext());
        MyDao dao = database.myDao();
        compositeDisposable = new CompositeDisposable();

        // Save the user details
        compositeDisposable.add(dao.updateUserDetails(new UserDetails(1, cigarettesPerDay, cigarettesPerPack, yearsOfSmoking, pricePerPackage))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MyDetailsActivity.this::saveCompleted));
    }

    private void saveCompleted() {
        Intent intent = new Intent(MyDetailsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Toast.makeText(MyDetailsActivity.this, "Güncelleme işlemi tamamlandı.", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}