package com.gokcekocal.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.adapters.DiaryAdapter;
import com.gokcekocal.myapplication.databinding.ActivityDiaryBinding;
import com.gokcekocal.myapplication.models.DailyNote;
import com.gokcekocal.myapplication.roomdb.MyAppDatabase;
import com.gokcekocal.myapplication.roomdb.MyDao;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DiaryActivity extends AppCompatActivity {

    private ActivityDiaryBinding binding;
    private CompositeDisposable compositeDisposable;
    private MyDao dao;
    private List<DailyNote> dailyNotes;
    private DiaryAdapter diaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyAppDatabase database = MyAppDatabase.getInstance(getApplicationContext());
        dao = database.myDao();

        getDiaryData();

        binding.addDiaryFab.setOnClickListener(view -> openAddDailyNoteDialog());


    }

    private void getDiaryData() {
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(dao.getAllDailyNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DiaryActivity.this::setRecyclerView));
    }


    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView(List<DailyNote> dataList) {
        dailyNotes = dataList;
        if (dailyNotes.size() == 0) {
            // No data
            binding.diaryRecyclerView.setVisibility(View.GONE);
            binding.noNoteLinearLayout.setVisibility(View.VISIBLE);
        } else {
            RecyclerView dailyRecycler = binding.diaryRecyclerView;

            dailyRecycler.setLayoutManager(new LinearLayoutManager(DiaryActivity.this));
            dailyRecycler.setHasFixedSize(true);

            diaryAdapter = new DiaryAdapter(dailyNotes, DiaryActivity.this);

            //Define animation for recycler
            recyclerItemAnimation(dailyRecycler);
            dailyRecycler.setAdapter(diaryAdapter);
            Objects.requireNonNull(diaryAdapter).notifyDataSetChanged();

            diaryAdapter.setListener((dailyNote, position) -> {
                dailyNotes.remove(dailyNote);
                diaryAdapter.notifyDataSetChanged();
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void openAddDailyNoteDialog() {
        Dialog dailyNoteDialog = new Dialog(DiaryActivity.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(DiaryActivity.this).inflate(R.layout.add_daily_note_dialog, null);
        Button addButton = view.findViewById(R.id.addDailyButton);
        ImageButton plusButton = view.findViewById(R.id.plusButton);
        ImageButton minusButton = view.findViewById(R.id.minusButton);
        TextView howManyTxt = view.findViewById(R.id.dialogHowManyTxt);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextInputEditText dailyTxt = view.findViewById(R.id.addDailyTxt);

        dailyNoteDialog.setContentView(view);
        dailyNoteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        plusButton.setOnClickListener(view2 -> {
            int howManyValue = Integer.parseInt(howManyTxt.getText().toString());
            howManyValue++;
            howManyTxt.setText(String.valueOf(howManyValue));
        });

        minusButton.setOnClickListener(view3 -> {
            int howManyValue = Integer.parseInt(howManyTxt.getText().toString());
            if (howManyValue > 0) {
                howManyValue--;
                howManyTxt.setText(String.valueOf(howManyValue));
            }
        });


        addButton.setOnClickListener(view1 -> {
            String dailyNoteString = Objects.requireNonNull(dailyTxt.getText()).toString();
            if (dailyNoteString.isEmpty()) {
                dailyNoteString = " ";
            }
            int rating = (int) ratingBar.getRating();
            int howMany = Integer.parseInt(howManyTxt.getText().toString());

            DailyNote dailyNote = new DailyNote(dailyNoteString, rating, System.currentTimeMillis(), howMany);

            //Save the new daily note
            compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(dao.insertDailyNote(dailyNote)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());
            if (binding.diaryRecyclerView.getVisibility() == View.GONE) {
                binding.diaryRecyclerView.setVisibility(View.VISIBLE);
                binding.noNoteLinearLayout.setVisibility(View.GONE);
            }
            dailyNotes.add(dailyNote);
            if (diaryAdapter != null) {
                diaryAdapter.notifyDataSetChanged();
            }
            dailyNoteDialog.cancel();
        });

        dailyNoteDialog.show();
    }


    private void recyclerItemAnimation(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        if (diaryAdapter != null) {
            diaryAdapter.clearMemory();
        }
    }
}