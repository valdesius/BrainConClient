package com.example.brainconclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.CourseListRecyclerViewHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.helpers.TestListRecyclerViewHelper;
import com.example.brainconclient.models.Course;
import com.example.brainconclient.models.Test;
import com.example.brainconclient.utils.MyVolleySingletonUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView courseDtlTitle, courseDtlBody;
    private Button createTestBtn;
    private RequestQueue mRequestQueue;
    private FloatingActionButton createCommentBtn;
    private SharedPreferences preferences;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Test> testList;


    private RequestQueue requestQueue;
    private Button deleteCourseBtn, updateCourseBtn;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        getSupportActionBar().hide();

        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);

        requestQueue = MyVolleySingletonUtil.getInstance(CourseDetailActivity.this).getRequestQueue();
        recyclerView = findViewById(R.id.test_list_recycler_view);

        courseDtlTitle = findViewById(R.id.course_dtl_title);
        courseDtlBody = findViewById(R.id.course_dtl_body);
        deleteCourseBtn = findViewById(R.id.delete_course_btn);
        createTestBtn = findViewById(R.id.create_test_btn);
        createCommentBtn = findViewById(R.id.create_comment_btn);

        updateCourseBtn = findViewById(R.id.update_course_btn);
        String courseId = getIntent().getStringExtra("course_id");
        String courseTitle = getIntent().getStringExtra("course_title");
        String courseBody = getIntent().getStringExtra("course_body");
        mRequestQueue = MyVolleySingletonUtil.getInstance(CourseDetailActivity.this).getRequestQueue();

        courseDtlTitle.setText(courseTitle);
        courseDtlBody.setText(courseBody);
        recyclerView.setLayoutManager(new LinearLayoutManager(CourseDetailActivity.this));
        testList = new ArrayList<>();
        createCommentFloatingActionButton();
        getUserTests();

        createTestBtn = findViewById(R.id.create_test_btn);
        createTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateTestActivity();
            }
        });
        createCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailActivity.this, CommentActivity.class);
                startActivity(intent);
            }
        });
        courseDtlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        updateCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailActivity.this, CourseSettingsActivity.class);
                intent.putExtra("course_id", courseId);
                intent.putExtra("course_title", courseTitle);
                intent.putExtra("course_body", courseBody);
                startActivity(intent);
            }
        });


        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CourseDetailActivity", "test id is: " + courseId);
                deleteCourse(courseId);
            }
        });
    }

    public void deleteCourse(String course_Id) {
        StringRequest request = new StringRequest(Request.Method.POST, ApiLinksHelper.deleteCourseApiUri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CourseDetailActivity", "Course Deleted Successfully!");
                Toast.makeText(CourseDetailActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CourseDetailActivity.this, MainActivity.class);
                startActivity(intent);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("CourseDetailActivity", "Failed to delete course with id of: " + course_Id);
                Toast.makeText(CourseDetailActivity.this, "Failed to delete course", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("course_id", course_Id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };


        requestQueue.add(request);
    }

    public void getUserTests() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", "email");
        String courseId = getIntent().getStringExtra("course_id");
        JsonArrayRequest jsonArrayRequest
                = new JsonArrayRequest(Request.Method.GET, ApiLinksHelper.getMyTestsApiUri(Integer.parseInt(courseId)), null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                recyclerView.setVisibility(View.VISIBLE);

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject responseObject = response.getJSONObject(i);
                        Test test
                                = new Test(responseObject.getInt("test_id"),
                                responseObject.getString("title"),
                                responseObject.getString("body"),
                                responseObject.getString("question"),
                                responseObject.getString("answer"))
                        ;
                        testList.add(test);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CourseDetailActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
                adapter = new TestListRecyclerViewHelper(testList, CourseDetailActivity.this);

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                progressBar.setVisibility(View.GONE);
                Log.i("MainActivity", error.toString());
                Toast.makeText(CourseDetailActivity.this, "Не удалось получить список курсов", Toast.LENGTH_LONG).show();
            }
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

    public void goToSuccessActivity() {
        Intent goToSuccess = new Intent(CourseDetailActivity.this, SuccessActivity.class);
        startActivity(goToSuccess);
        finish();
    }

    public void createCommentFloatingActionButton() {
        createTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateCommentActivity();
            }
        });
    }

    public void goToCreateTestActivity() {
        Intent goToCreateCourse = new Intent(CourseDetailActivity.this, CreateTestActivity.class);
        String courseId = getIntent().getStringExtra("course_id");
        goToCreateCourse.putExtra("course_id", courseId);
        startActivity(goToCreateCourse);
    }

    public void goToCreateCommentActivity() {
        Intent goToCreateCourse = new Intent(CourseDetailActivity.this, CommentActivity.class);
        startActivity(goToCreateCourse);
    }

}