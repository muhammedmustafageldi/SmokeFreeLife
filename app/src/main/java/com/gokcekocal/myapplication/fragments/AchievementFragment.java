package com.gokcekocal.myapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.adapters.DaySuccessAdapter;
import com.gokcekocal.myapplication.databinding.FragmentAchievementBinding;
import com.gokcekocal.myapplication.models.DaySuccess;

import java.util.ArrayList;
import java.util.List;

public class AchievementFragment extends Fragment {

    private FragmentAchievementBinding binding;

    public AchievementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAchievementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Başarılar");
            toolbar.setNavigationIcon(R.drawable.ico_achievement);
        }

        setAchievementList();
    }

    private void setAchievementList() {
        List<DaySuccess> daySuccesses = new ArrayList<>();

        daySuccesses.add(new DaySuccess("Başarı seviyesi: 1", R.drawable.day1gray, R.drawable.day1gold, 1));
        daySuccesses.add(new DaySuccess("Başarı seviyesi: 3", R.drawable.day3gray, R.drawable.day3gold, 3));
        daySuccesses.add(new DaySuccess("Başarı seviyesi: 7", R.drawable.day7gray, R.drawable.day7gold, 7));
        daySuccesses.add(new DaySuccess("Başarı seviyesi: 10", R.drawable.day10gray, R.drawable.day10gold, 10));
        daySuccesses.add(new DaySuccess("Başarı seviyesi: 14", R.drawable.day14gray, R.drawable.day14gold, 14));
        daySuccesses.add(new DaySuccess("Başarı seviyesi: 30", R.drawable.day30gray, R.drawable.day30gold, 30));
        daySuccesses.add(new DaySuccess("Başarı seviyesi: 90", R.drawable.day90gray, R.drawable.day90gold, 90));
        daySuccesses.add(new DaySuccess("Başarı seviyesi: 180", R.drawable.day180gray, R.drawable.day180gold, 180));
        daySuccesses.add(new DaySuccess("Başarı seviyesi: 365", R.drawable.day365gray, R.drawable.day365gold, 365));

        initRecycler(daySuccesses);
    }


    private void initRecycler(List<DaySuccess> daySuccesses) {
        RecyclerView dayRecycler = binding.daySuccessRecycler;

        dayRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        dayRecycler.setHasFixedSize(true);

        //Get user time for send the adapter
        long userTime = 0;
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            // User's time in seconds
            userTime = sharedPreferences.getLong("userTime", 0);
        }
        DaySuccessAdapter daySuccessAdapter = new DaySuccessAdapter(daySuccesses, secondsToDay(userTime));

        //Define animation for recycler
        recyclerItemAnimation(dayRecycler);

        dayRecycler.setAdapter(daySuccessAdapter);
    }

    private void recyclerItemAnimation(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    private int secondsToDay(long userTime) {
        long currentTime = System.currentTimeMillis() / 1000;
        long timeDifference = currentTime - userTime;

        int secondsInMinute = 60;
        int minutesInHour = 60;
        int hoursInDay = 24;
        int secondsInDay = secondsInMinute * minutesInHour * hoursInDay;

        long daysDifference = timeDifference / secondsInDay;
        return (int) daysDifference;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}