package com.gokcekocal.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gokcekocal.myapplication.R;
import com.gokcekocal.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.gokcekocal.myapplication.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        assert navHostFragment != null;
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navHostFragment.getNavController());

        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.action_harshRealities) {
            intent = new Intent(MainActivity.this, HarshRealitiesActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_my_diary) {
            intent = new Intent(MainActivity.this, DiaryActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_myDetails) {
            intent = new Intent(MainActivity.this, MyDetailsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}