package com.example.brainconclient;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    private Button addCourseButtonOrAllCourses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        addCourseButtonOrAllCourses = view.findViewById(R.id.button_html);

        // Получаем сохраненную роль пользователя
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("role", "");

        // Устанавливаем текст кнопки в зависимости от роли
        if ("STUDENT".equals(userRole)) {
            addCourseButtonOrAllCourses.setText("Записаться на курс");
        } else if ("MENTOR".equals(userRole)) {
            addCourseButtonOrAllCourses.setText("Создать курс");
        }
        return view;
    }
}