package com.example.brainconclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private TextView noteDtlTitle,  noteDtlBody;
    private RequestQueue mRequestQueue;
    private FloatingActionButton createTestBtn;
    private SharedPreferences preferences;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Test> testList;

    private RequestQueue requestQueue;
    private TextView deleteNoteBtn;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // SET / GET PREFERENCES OBJECT:
        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);

        // INITIATE REQUEST QUE:
        requestQueue = MyVolleySingletonUtil.getInstance(CourseDetailActivity.this).getRequestQueue();
        recyclerView = findViewById(R.id.test_list_recycler_view);
        // HOOK / INITIATE VIEW ELEMENTS / COMPONENTS:
        noteDtlTitle    = findViewById(R.id.note_dtl_title);
        noteDtlBody     = findViewById(R.id.note_dtl_body);
        deleteNoteBtn   = findViewById(R.id.delete_course_btn);
        createTestBtn = findViewById(R.id.create_test_btn);

        // GET INTEND DATA:
        String noteId   = getIntent().getStringExtra("note_id");
        String noteTitle = getIntent().getStringExtra("note_tile");
        String noteBody = getIntent().getStringExtra("note_body");
        mRequestQueue = MyVolleySingletonUtil.getInstance(CourseDetailActivity.this).getRequestQueue();

        // SET VALUES TO VIEW COMPONENTS:
        noteDtlTitle.setText(noteTitle);
        noteDtlBody.setText(noteBody);
        recyclerView.setLayoutManager(new LinearLayoutManager(CourseDetailActivity.this));
        testList = new ArrayList<>();
createTestFloatingActionButton();
getUserTests();

        createTestBtn = findViewById(R.id.create_test_btn);

        // DELETE NOTE ON CLICK LISTENER OBJECT:
        deleteNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("NoteDetailActivity", "note id is: " + noteId);
                deleteNote(noteId);
            }
            // END OF ON CLICK METHOD.
        });
        // END OF DELETE NOTE ON CLICK LISTENER OBJECT.
    }
    // END OF ON CREATE METHOD.

    public void deleteNote(String note_Id){
        StringRequest request = new StringRequest(Request.Method.POST, ApiLinksHelper.deleteNoteApiUri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("NoteDetailActivity","Noted Deleted Successfully!");
                Toast.makeText(CourseDetailActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CourseDetailActivity.this, MainActivity.class);
                startActivity(intent);

            }
            // END OF ON RESPONSE METHOD.

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("NoteDetailActivity", "Failed to delete note with id of: " + note_Id);
                Toast.makeText(CourseDetailActivity.this, "Failed to delete note", Toast.LENGTH_LONG).show();
            }
            // END OF ON ERROR RESPONSE METHOD.
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("note_id", note_Id);
                return params;
            }
            // END OF GET PARAMS METHOD.

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               String token = preferences.getString("token", "");
               Map<String, String> headers = new HashMap<>();
               headers.put("Authorization", "Bearer " + token);
               return headers;
            }
            // END OF GET HEADERS METHOD.
        };
        // END OF STRING REQUEST OBJECT.

        // ADD / SEND REQUEST:
        requestQueue.add(request);
    }

    public void getUserTests() {
        // SET USER DATA MAP OBJECT:
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", "email");

        JsonArrayRequest jsonArrayRequest
                = new JsonArrayRequest(Request.Method.GET, ApiLinksHelper.getMyTestsApiUri(), null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
//                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject responseObject = response.getJSONObject(i);
                        Test test
                                = new Test(responseObject.getInt("test_id"),
                                responseObject.getString("title"),
                                responseObject.getString("body"));
                        testList.add(test);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CourseDetailActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    // END OF TRY BLOCK
                }
                // END OF RESPONSE FOR LOOP.
                adapter = new TestListRecyclerViewHelper(testList, CourseDetailActivity.this);

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
                Toast.makeText(CourseDetailActivity.this, "Не удалось получить список курсов", Toast.LENGTH_LONG).show();
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
    // END OF DELETE NOTE METHOD.

    public void goToSuccessActivity(){
        Intent goToSuccess = new Intent(CourseDetailActivity.this, SuccessActivity.class);
        startActivity(goToSuccess);
        finish();
    }

    public void createTestFloatingActionButton() {
        createTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateTestActivity();
            }
            // END ON CLICK METHOD.
        });
    }
    public void goToCreateTestActivity() {
        Intent goToCreateNote = new Intent(CourseDetailActivity.this, CreateTestActivity.class);
        startActivity(goToCreateNote);
    }
    // END OF GO TO SUCCESS ACTIVITY METHOD.

}
// END OF NOTE DETAIL ACTIVITY CLASS.