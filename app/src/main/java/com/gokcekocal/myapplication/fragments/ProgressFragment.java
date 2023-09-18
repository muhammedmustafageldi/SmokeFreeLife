package com.gokcekocal.myapplication.fragments;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.databinding.FragmentProgressBinding;
import com.gokcekocal.myapplication.models.UserDetails;
import com.gokcekocal.myapplication.roomdb.MyAppDatabase;
import com.gokcekocal.myapplication.roomdb.MyDao;
import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProgressFragment extends Fragment {

    private FragmentProgressBinding binding;
    private CompositeDisposable compositeDisposable;

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProgressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("İlerleme");
            toolbar.setNavigationIcon(R.drawable.ico_progress);
        }

        getUserData();

        binding.giveUp.setOnClickListener(view2 -> {
            showAlertDialog();
        });
    }


    private void getUserData() {
        MyAppDatabase database = MyAppDatabase.getInstance(requireContext());
        MyDao dao = database.myDao();

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(dao.getAllUserDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ProgressFragment.this::calculateUserData));
    }

    @SuppressLint("SetTextI18n")
    private void calculateUserTimer(long userTime, UserDetails userDetails) {
        long currentTime = System.currentTimeMillis() / 1000;
        long timeDifference = currentTime - userTime;

        int secondsInMinute = 60;
        int minutesInHour = 60;
        int hoursInDay = 24;
        int daysInMonth = 30;

        int minutes = (int) (timeDifference / secondsInMinute);
        int hours = minutes / minutesInHour;
        int days = hours / hoursInDay;
        int months = days / daysInMonth;


        if (months == 0) {
            binding.timeTxt1.setText(days + "\ngün");
            binding.timeTxt2.setText(hours % 24 + "\nsaat");
            binding.timeTxt3.setText(minutes % 60 + "\ndakika");
        } else {
            binding.timeTxt1.setText(months + "\nay");
            binding.timeTxt2.setText(days % 30 + "\ngün");
            binding.timeTxt3.setText(hours % 24 + "\nsaat");
        }

        // How many days has it been?
        binding.freeDayTxt.setText(String.valueOf(days));

        // How many cigarettes were not smoked?
        int cigarettesPerDay = userDetails.getCigarettesPerDay();
        float numberOfMinutesPerHour = 60f;
        float cigarettePerMinute = cigarettesPerDay / (24 * numberOfMinutesPerHour);
        float nonSmoking = cigarettePerMinute * minutes;

        binding.nonSmokingTxt.setText(formatFloatValue(nonSmoking));

        // How much money was recovered
        float priceOfOneCigarette = userDetails.getPricePerPackage() / userDetails.getCigarettesPerPack();
        float moneyRecovered = priceOfOneCigarette * nonSmoking;

        binding.saveMoneyTxt.setText(formatFloatValue(moneyRecovered) + " ₺");

        // How much life I've earned
        int numberOfMinutesOfDay = 24 * 60;
        float timeMinutesGained = nonSmoking * 12;
        float timeDayGained = timeMinutesGained / numberOfMinutesOfDay;

        if (timeDayGained > 30) {
            binding.saveTimeTxt.setText(formatFloatValue2(timeDayGained) + "a");
        } else {
            binding.saveTimeTxt.setText(formatFloatValue2(timeDayGained) + "g");
        }
    }

    public String formatFloatValue(float value) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(value);
    }

    public String formatFloatValue2(float value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }

    @SuppressLint("SetTextI18n")
    private void calculateUserData(List<UserDetails> userDetails) {
        UserDetails userDetails1 = userDetails.get(0);
        int cigarettesPerDay = userDetails1.getCigarettesPerDay();
        int cigarettesPerPack = userDetails1.getCigarettesPerPack();
        int yearsOfSmoking = userDetails1.getYearsOfSmoking();
        float pricePerPackage = userDetails1.getPricePerPackage();

        // Calculates
        int totalCigarettesNumber = cigarettesPerDay * 365 * yearsOfSmoking;
        int totalPackNumber = totalCigarettesNumber / cigarettesPerPack;
        float totalExpenditure = totalPackNumber * pricePerPackage;
        int wastedTimeMinute = totalCigarettesNumber * 12;
        int month = wastedTimeMinute / (60 * 24 * 30); // mouth
        int day = (wastedTimeMinute % (60 * 24 * 30)) / (60 * 24); // day
        int hour = (wastedTimeMinute % (60 * 24)) / 60; // hour

        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        String formattedValueMoney = decimalFormat.format(totalExpenditure);

        String formattedValueSmokedNumber = decimalFormat.format(totalCigarettesNumber);

        animateTextView(totalCigarettesNumber, binding.cigarettesSmokedTxt);
        animateTextView(totalExpenditure, binding.wastedMoneyTxt);

        //binding.cigarettesSmokedTxt.setText(formattedValueSmokedNumber);
        //binding.wastedMoneyTxt.setText(formattedValueMoney + " ₺");

        binding.wastedTimeTxt.setText(month + "A " + day + "G " + hour + "S");

        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            // User's time in seconds
            long userTime = sharedPreferences.getLong("userTime", 0);
            calculateUserTimer(userTime, userDetails1);
        }
    }

    private void animateTextView(float targetValue, TextView textView) {
        float startValue = 0f;

        ValueAnimator animator = ValueAnimator.ofFloat(startValue, targetValue);
        animator.setDuration(3000);
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            DecimalFormat decimalFormat = new DecimalFormat("#,##0");
            String formattedValue = decimalFormat.format(animatedValue);
            textView.setText(formattedValue);
        });
        animator.start();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Emin misin?");
        builder.setIcon(R.drawable.ico_alert);
        builder.setMessage("Eğer tekrar kullandıysanız, kendinizi zayıf görmeyin. Bırakma yolunda bir veya iki kez tökezlemek zayıflık değildir.");
        builder.setPositiveButton("Zamanlayıcıyı Sıfırla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getActivity() != null) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("userTime");
                    long currentTimeInSeconds = System.currentTimeMillis() / 1000;
                    editor.putLong("userTime", currentTimeInSeconds);
                    editor.apply();
                    getUserData();
                }
            }
        });
        builder.setNegativeButton("İptal", (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}