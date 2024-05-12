package com.example.brainconclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private TextView profileLogoutBtn;
    private SharedPreferences prefs; // Добавлена переменная для SharedPreferences

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Инициализация SharedPreferences
        prefs = getActivity().getSharedPreferences("YourPrefsName", Context.MODE_PRIVATE);

        // Инициализация кнопки
        profileLogoutBtn = view.findViewById(R.id.txt_logout);

        // Установка обработчика нажатия кнопки
        profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Действия при нажатии на кнопку
                logUserOut();
            }
        });

        return view;
    }

    public void clearPreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void goToLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // getActivity().finish(); // Эта строка больше не нужна, так как флаги Intent очистят стек
    }

    // Метод для выхода из профиля
    public void logUserOut(){
        clearPreferences();
        // Устанавливаем результат для активности
        getActivity().setResult(Activity.RESULT_OK);
        goToLogin();
    }
}