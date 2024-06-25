package com.example.brainconclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;

import java.util.HashMap;
import java.util.Map;

public class CourseSettingsActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private EditText courseTitleField;
    private EditText courseBodyField;
    private Button updateCourseButton;
    private SharedPreferences prefs;
    private int courseId; // ID курса должен быть передан в эту активность

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_settings);

        // Инициализация ActionBar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Изменение данных курса");

        // Инициализация полей ввода и кнопки
        courseTitleField = findViewById(R.id.update_course_title_field);
        courseBodyField = findViewById(R.id.update_course_body_field);
        updateCourseButton = findViewById(R.id.update_course_btn);

        // Получение данных из Intent
        Intent intent = getIntent();
        if (intent != null) {
            courseId = intent.getIntExtra("course_id", -1); // Получаем ID курса
            String courseTitle = intent.getStringExtra("course_title"); // Получаем название курса
            String courseBody = intent.getStringExtra("course_body"); // Получаем описание курса

            // Заполнение полей данными
            courseTitleField.setText(courseTitle);
            courseBodyField.setText(courseBody);
        }

        // Установка обработчика нажатия на кнопку
        updateCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourse();
            }
        });

        // Здесь может быть ваш код для получения данных курса с сервера, если это необходимо
    }

    private void updateCourse() {
        // Получаем обновленные значения из полей ввода:
        String courseTitle = courseTitleField.getText().toString().trim();
        String courseBody = courseBodyField.getText().toString().trim();

        // Создаем новый запрос на обновление курса:
        StringRequest updateCourseRequest = new StringRequest(Request.Method.PUT, ApiLinksHelper.updateCourseApiUri(),
                response -> {
                    // Обработка успешного ответа:
                    Toast.makeText(CourseSettingsActivity.this, "Курс успешно обновлен", Toast.LENGTH_SHORT).show();
                    // Можно здесь обновить SharedPreferences, если необходимо
                },
                error -> {
                    // Обработка ошибки:
                    Toast.makeText(CourseSettingsActivity.this, "Ошибка обновления курса", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("course_title", courseTitle);
                params.put("course_body", courseBody);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Добавьте заголовки, если это необходимо, например, токен авторизации:
                headers.put("Authorization", "Bearer " + prefs.getString("token", ""));
                return headers;
            }
        };

        // Добавляем запрос в очередь запросов:
        requestQueue.add(updateCourseRequest);
    }


    // Дополнительные методы, если они вам нужны
}


//    private void updateCourse() {
//        String title = courseTitleField.getText().toString();
//        String body = courseBodyField.getText().toString();
//
//        // Создание объекта запроса
//        UpdateCourseRequest updateCourseRequest = new UpdateCourseRequest();
//        updateCourseRequest.setTitle(title);
//        updateCourseRequest.setBody(body);
//        updateCourseRequest.setCourse_id(courseId);
//
//        // Отправка запроса на обновление курса
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        CourseApi courseApi = retrofit.create(CourseApi.class);
//        Call<CourseCreatedResponse> call = courseApi.updateCourseById(updateCourseRequest);
//
//        call.enqueue(new Callback<CourseCreatedResponse>() {
//            @Override
//            public void onResponse(Call<CourseCreatedResponse> call, Response<CourseCreatedResponse> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(CourseSettingsActivity.this, "Курс обновлен!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(CourseSettingsActivity.this, "Произошла ошибка при обновлении курса", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CourseCreatedResponse> call, Throwable t) {
//                Toast.makeText(CourseSettingsActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
//            }
//        });
