package com.example.brainconclient;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.utils.MyVolleySingletonUtil;

import java.util.HashMap;
import java.util.Map;

public class CreateCourseActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private RequestQueue requestQueue;

    private SharedPreferences preferences;
    private TextView createNoteTitleField, createNoteBodyField;
    private Button createNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        // SET ACTION BAR ATTRIBUTES:


        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);

        // HOOK / INITIATE VIEW ELEMENTS / COMPONENTS:
        createNoteTitleField    = findViewById(R.id.create_note_title_field);
        createNoteBodyField     = findViewById(R.id.create_note_body_field);
        createNoteBtn           = findViewById(R.id.create_note_btn);

        // INITIATE REQUEST QUE:
        requestQueue = MyVolleySingletonUtil.getInstance(CreateCourseActivity.this).getRequestQueue();

        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CREATE NOTE METHOD:
                createNote();
            }
        });
        // END OF CREATE NOTE ON CLICK LISTENER OBJECT.

    }
    // END OF ON CREATE METHOD.

    public void createNote(){
        String title = createNoteTitleField.getText().toString();
        String body = createNoteBodyField.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, ApiLinksHelper.createNoteApiUri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CreateCourseActivity", "Created Course!");
                Toast.makeText(CreateCourseActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("CreateCourseActivity", "Ошибка в создании курса");
                Toast.makeText(CreateCourseActivity.this, "Failed to create note", Toast.LENGTH_LONG).show();
            }
            // END OF ON ERROR RESPONSE METHOD.
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("body", body);
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
    // END OF CREATE NOTE METHOD.

    public void goToSuccessActivity(){
        Intent goToSuccess = new Intent(CreateCourseActivity.this, SuccessActivity.class);
        startActivity(goToSuccess);
        finish();
    }
    // END OF GO TO SUCCESS ACTIVITY METHOD.
}
// END OF CREATE NOTE ACTIVITY CLASS.