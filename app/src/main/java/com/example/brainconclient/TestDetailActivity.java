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
import com.example.brainconclient.models.Test;
import com.example.brainconclient.utils.MyVolleySingletonUtil;

import java.util.HashMap;
import java.util.Map;

public class TestDetailActivity extends AppCompatActivity {

    private TextView testDtlTitle,  testDtlBody, testDtlQuestion;
    private SharedPreferences preferences;

    private RequestQueue requestQueue;
    private TextView deleteTestBtn;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);

        // SET / GET PREFERENCES OBJECT:
        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);

        // INITIATE REQUEST QUE:
        requestQueue = MyVolleySingletonUtil.getInstance(TestDetailActivity.this).getRequestQueue();

        // HOOK / INITIATE VIEW ELEMENTS / COMPONENTS:
        testDtlTitle    = findViewById(R.id.test_dtl_title);
        testDtlBody     = findViewById(R.id.test_dtl_body);
        testDtlQuestion = findViewById(R.id.test_dtl_question);
        deleteTestBtn   = findViewById(R.id.delete_test_btn);

        // GET INTEND DATA:
        String testId   = getIntent().getStringExtra("test_id");
        String testTitle = getIntent().getStringExtra("test_title");
        String testBody = getIntent().getStringExtra("test_body");
        String testQuestion = getIntent().getStringExtra("test_question");

        // SET VALUES TO VIEW COMPONENTS:
        testDtlTitle.setText(testTitle);
        testDtlBody.setText(testBody);
        testDtlQuestion.setText(testQuestion);

        // DELETE NOTE ON CLICK LISTENER OBJECT:
        deleteTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TestDetailActivity", "test id is: " + testId);
                deleteTest(testId);
            }
            // END OF ON CLICK METHOD.
        });
        // END OF DELETE NOTE ON CLICK LISTENER OBJECT.
    }
    // END OF ON CREATE METHOD.

    public void deleteTest(String test_Id){
        StringRequest request = new StringRequest(Request.Method.POST, ApiLinksHelper.deleteTestApiUri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TestDetailActivity","Test Deleted Successfully!");
                Toast.makeText(TestDetailActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(TestDetailActivity.this, MainActivity.class);
                startActivity(intent);

            }
            // END OF ON RESPONSE METHOD.

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("TestDetailActivity", "Failed to delete test with id of: " + test_Id);
                Toast.makeText(TestDetailActivity.this, "Failed to delete test", Toast.LENGTH_LONG).show();
            }
            // END OF ON ERROR RESPONSE METHOD.
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("test_id", test_Id);
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


    // END OF GO TO SUCCESS ACTIVITY METHOD.

}
// END OF NOTE DETAIL ACTIVITY CLASS.