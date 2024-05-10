package com.example.brainconclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

// Устанавливаем начальный фрагмент
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

// Создаем значок уведомления (badge) для пункта меню "Поиск"
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.search);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(8);

// Обработчик выбора пунктов нижнего навигационного меню
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;
            } else if (itemId == R.id.profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
            } else if (itemId == R.id.search) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                return true;
            }
            return false;
        });
    }
}