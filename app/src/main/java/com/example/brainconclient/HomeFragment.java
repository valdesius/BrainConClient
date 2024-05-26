package com.example.brainconclient;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
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
import java.util.Objects;

public class HomeFragment extends Fragment {
    private RequestQueue mRequestQueue;
    private RecyclerView.Adapter adapter;
    private List<Course> courseList;
    private List<Course> favoriteCourseList;
    private RecyclerView recyclerView;
    private FloatingActionButton createCourseBtn;
    SharedPreferences preferences;
    private Button addCourseButtonOrAllCourses;

    TextView displayUsername;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        preferences = getActivity().getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);
        createCourseBtn = view.findViewById(R.id.create_courses_btn);
//        displayUsername = view.findViewById(R.id.display_username);

        recyclerView = view.findViewById(R.id.note_list_recycler_view);

//        setDisplayUsername();

        // Получаем сохраненную роль пользователя

        mRequestQueue = MyVolleySingletonUtil.getInstance(getActivity()).getRequestQueue();

        // Устанавливаем текст кнопки в зависимости от роли
        createNoteFloatingActionButton();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseList = new ArrayList<>();
        getUserNotes();
        return view;
    }

    public void getUserNotes() {
        // SET USER DATA MAP OBJECT:
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", "email");

        JsonArrayRequest jsonArrayRequest
                = new JsonArrayRequest(Request.Method.GET, ApiLinksHelper.getMyNotesApiUri(), null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
//                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                filterFavoriteCourses(response);

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject responseObject = response.getJSONObject(i);
                        Course note
                                = new Course(responseObject.getInt("note_id"),
                                responseObject.getString("title"),
                                responseObject.getString("body"));
                        courseList.add(note);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    // END OF TRY BLOCK
                }
                // END OF RESPONSE FOR LOOP.
                adapter = new CourseListRecyclerViewHelper(courseList, getActivity());

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
            // END OF ON SUCCESS RESPONSE METHOD.
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                progressBar.setVisibility(View.GONE);
                Log.i("MainActivity", error.toString());
                Toast.makeText(getActivity(), "Не удалось получить список курсов", Toast.LENGTH_LONG).show();
            }
            // END OF ON ERROR RESPONSE METHOD.
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
        //  END OF JSON ARRAY REQUEST OBJECT.

        // ADD / RUN REQUEST QUE:
    }



    public void createNoteFloatingActionButton() {
        createCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateNoteActivity();
            }
            // END ON CLICK METHOD.
        });
    }

    public void goToCreateNoteActivity() {
        Intent goToCreateNote = new Intent(getActivity(), CreateCourseActivity.class);
        startActivity(goToCreateNote);
    }
    private void filterFavoriteCourses(JSONArray courses) {
        favoriteCourseList = new ArrayList<>();
        for (int i = 0; i < courses.length(); i++) {
            try {
                JSONObject courseObject = courses.getJSONObject(i);
                boolean isFavorite = courseObject.getBoolean("is_favorite"); // Предполагаем, что у вас есть такой флаг
                if (isFavorite) {
                    Course course = new Course(
                            courseObject.getInt("note_id"),
                            courseObject.getString("title"),
                            courseObject.getString("body")
                    );
                    favoriteCourseList.add(course);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        updateUIWithFavorites();
    }

    // Метод для обновления UI избранными курсами
    private void updateUIWithFavorites() {
        adapter = new CourseListRecyclerViewHelper(favoriteCourseList, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}