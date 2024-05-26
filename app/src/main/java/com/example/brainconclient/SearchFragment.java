package com.example.brainconclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.brainconclient.helpers.ApiLinksHelper;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.brainconclient.helpers.CourseListRecyclerViewHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.models.Course;
import com.example.brainconclient.utils.MyVolleySingletonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchFragment extends Fragment {
    private EditText searchEditText;
    private RecyclerView searchResultsRecyclerView;
    private CourseListRecyclerViewHelper adapter;
    private List<Course> allCourses;
    private List<Course> filteredCourses;
    private RequestQueue requestQueue;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view);
        preferences = getActivity().getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), Context.MODE_PRIVATE);
        requestQueue = MyVolleySingletonUtil.getInstance(getActivity()).getRequestQueue();
        // Инициализация списка всех курсов и адаптера
        allCourses = getAllCourses(); // Здесь должен быть ваш метод для получения всех курсов
        filteredCourses = new ArrayList<>(allCourses);
        adapter = new CourseListRecyclerViewHelper(filteredCourses, getActivity());
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchResultsRecyclerView.setAdapter(adapter);

        setupSearchEditText();
        return view;
    }

    private void setupSearchEditText() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Ничего не делаем
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Фильтрация списка курсов в соответствии с введенным текстом
                filterCourses(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Ничего не делаем
            }
        });
    }

    private void filterCourses(String text) {
        filteredCourses.clear();
        for (Course course : allCourses) {
            if (course.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredCourses.add(course);
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Метод для получения всех курсов (заглушка)
    private List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        // Создаем запрос на получение всех курсов
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                ApiLinksHelper.getMyNotesApiUri(), // Замените на ваш URL для получения всех курсов
                null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject courseObject = response.getJSONObject(i);
                            Course course = new Course(
                                    courseObject.getInt("note_id"),
                                    courseObject.getString("title"),
                                    courseObject.getString("body") // Предполагаем, что у курса есть описание
                            );
                            courses.add(course);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged(); // Обновляем адаптер после получения данных
                },
                error -> Log.e("SearchFragment", "Error: " + error.getMessage())
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("content-type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
        return courses;
    }
}