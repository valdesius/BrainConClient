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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.models.Course;
import com.example.brainconclient.utils.MyVolleySingletonUtil;


import java.util.HashMap;
import java.util.Map;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView noteDtlTitle,  noteDtlBody;
    private SharedPreferences preferences;

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

        // HOOK / INITIATE VIEW ELEMENTS / COMPONENTS:
        noteDtlTitle    = findViewById(R.id.note_dtl_title);
        noteDtlBody     = findViewById(R.id.note_dtl_body);
        deleteNoteBtn   = findViewById(R.id.delete_course_btn);

        // GET INTEND DATA:
        String noteId   = getIntent().getStringExtra("note_id");
        String noteTitle = getIntent().getStringExtra("note_tile");
        String noteBody = getIntent().getStringExtra("note_body");

        // SET VALUES TO VIEW COMPONENTS:
        noteDtlTitle.setText(noteTitle);
        noteDtlBody.setText(noteBody);

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
    // END OF DELETE NOTE METHOD.

    public void goToSuccessActivity(){
        Intent goToSuccess = new Intent(CourseDetailActivity.this, SuccessActivity.class);
        startActivity(goToSuccess);
        finish();
    }
    // END OF GO TO SUCCESS ACTIVITY METHOD.

}
// END OF NOTE DETAIL ACTIVITY CLASS.