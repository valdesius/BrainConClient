package com.example.brainconclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
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

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private CourseListRecyclerViewHelper adapter;
    private List<Course> favoriteCourses;
    private RequestQueue requestQueue;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.favorite_recycler_view);
        preferences = getActivity().getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), Context.MODE_PRIVATE);
        requestQueue = MyVolleySingletonUtil.getInstance(getActivity()).getRequestQueue();
        loadFavoriteCourses();
        return view;
    }

    private void loadFavoriteCourses() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                ApiLinksHelper.getMyCoursesApiUri(),
                null,
                response -> {
                    favoriteCourses = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject responseObject = response.getJSONObject(i);
                            int courseId = responseObject.getInt("course_id");
                            String title = responseObject.getString("title");
                            String body = responseObject.getString("body");
                            boolean isFavorite = preferences.getBoolean("favorite_" + courseId, false);

                            if (isFavorite) {
                                Course course = new Course(courseId, title, body);
                                favoriteCourses.add(course);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                    adapter = new CourseListRecyclerViewHelper(favoriteCourses, getActivity(), preferences.getBoolean("isGuest", false));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Не удалось получить список курсов", Toast.LENGTH_LONG).show();
                }
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
    }
}