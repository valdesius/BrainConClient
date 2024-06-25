package com.example.brainconclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.CourseGuestListRecyclerViewHelper;
import com.example.brainconclient.helpers.CourseListRecyclerViewHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.models.Course;
import com.example.brainconclient.utils.MyVolleySingletonUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private RequestQueue mRequestQueue;
    private RecyclerView.Adapter adapter;
    private List<Course> courseList;
    private List<Course> favoriteCourseList;
    private RecyclerView recyclerView;
    private FloatingActionButton createCourseBtn;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        preferences = getActivity().getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), Context.MODE_PRIVATE);
        createCourseBtn = view.findViewById(R.id.create_courses_btn);
        recyclerView = view.findViewById(R.id.course_list_recycler_view);

        boolean isGuest = getActivity().getIntent().getBooleanExtra("isGuest", false);
        recyclerView.setClickable(!isGuest);

        if (isGuest) {
            createCourseBtn.setVisibility(View.GONE);
        } else {
            createCourseFloatingActionButton();
        }

        mRequestQueue = MyVolleySingletonUtil.getInstance(getActivity()).getRequestQueue();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseList = new ArrayList<>();
        getUserCourses(getActivity());
        return view;
    }

    public void getUserCourses(Context context) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", "email");
        boolean isGuest = getActivity().getIntent().getBooleanExtra("isGuest", false);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ApiLinksHelper.getMyCoursesApiUri(), null,
                response -> {
                    recyclerView.setVisibility(View.VISIBLE);
                    filterFavoriteCourses(response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject responseObject = response.getJSONObject(i);
                            Course course = new Course(responseObject.getInt("course_id"),
                                    responseObject.getString("title"),
                                    responseObject.getString("body"));
                            courseList.add(course);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                    if (isGuest) {
                        adapter = new CourseGuestListRecyclerViewHelper(courseList, context, preferences.getBoolean("isGuest", false));
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = new CourseListRecyclerViewHelper(courseList, context, preferences.getBoolean("isGuest", false));
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.i("HomeFragment", error.toString());
                    Toast.makeText(context, "Не удалось получить список курсов", Toast.LENGTH_LONG).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("content-type", "application/json");
                return headers;
            }
        };
        mRequestQueue.add(jsonArrayRequest);
    }

    public void createCourseFloatingActionButton() {
        createCourseBtn.setOnClickListener(v -> goToCreateCourseActivity());
    }


    public void goToCreateCourseActivity() {
        Intent goToCreateCourse = new Intent(getActivity(), CreateCourseActivity.class);
        startActivity(goToCreateCourse);
    }

    private void filterFavoriteCourses(JSONArray courses) {
        favoriteCourseList = new ArrayList<>();
        for (int i = 0; i < courses.length(); i++) {
            try {
                JSONObject courseObject = courses.getJSONObject(i);
                Course course = new Course(
                        courseObject.getInt("course_id"),
                        courseObject.getString("title"),
                        courseObject.getString("body")
                );
                favoriteCourseList.add(course);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        updateUIWithFavorites();
    }

    private void updateUIWithFavorites() {
        adapter = new CourseListRecyclerViewHelper(favoriteCourseList, getActivity(), preferences.getBoolean("isGuest", false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}