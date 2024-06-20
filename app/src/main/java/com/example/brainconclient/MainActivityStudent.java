package com.example.brainconclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityStudent extends AppCompatActivity {
    private SharedPreferences prefs;
    private boolean isLoggedIn;

    BottomNavigationView bottomNavigationView;

    HomeFragmentsStudent homeFragment = new HomeFragmentsStudent();
    SearchFragment searchFragment = new SearchFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    FavoriteFragment favoriteFragment = new FavoriteFragment();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        boolean isGuest = getIntent().getBooleanExtra("isGuest", false);
        prefs = getSharedPreferences("YourPrefsName", Context.MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear all previous activities
            startActivity(intent);
            finish();
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

// Устанавливаем начальный фрагмент
        getSupportFragmentManager().beginTransaction().replace(R.id.containerr, homeFragment).commit();

// Создаем значок уведомления (badge) для пункта меню "Поиск"
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.search);



// Обработчик выбора пунктов нижнего навигационного меню
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (isGuest) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerr, homeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerr, searchFragment).commit();
                    return true;
                }
                // Гость не может перейти на другие страницы
                return false;
            } else {
                // Пользователь вошел в систему, доступ ко всем фрагментам
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerr, homeFragment).commit();
                } else if (item.getItemId() == R.id.search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerr, searchFragment).commit();
                } else if (item.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerr, profileFragment).commit();
                } else if (item.getItemId() == R.id.fav) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerr, favoriteFragment).commit();
                } else {
                    // Если ни один из известных пунктов меню не выбран
                    return false;
                }
                return true;
            }
        });
    }

}