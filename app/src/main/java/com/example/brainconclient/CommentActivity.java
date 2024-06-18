package com.example.brainconclient;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.brainconclient.helpers.ApiLinksHelper;
import com.example.brainconclient.helpers.CommentListRecyclerViewHelper;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.helpers.TestListRecyclerViewHelper;
import com.example.brainconclient.models.Comment;
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

public class CommentActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private ActionBar actionBar;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private RequestQueue mRequestQueue;
    private List<Comment> commentList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Комментарии");

        requestQueue = MyVolleySingletonUtil.getInstance(CommentActivity.this).getRequestQueue();
        recyclerView = findViewById(R.id.comment_list_recycler_view);
        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);

        mRequestQueue = MyVolleySingletonUtil.getInstance(CommentActivity.this).getRequestQueue();

        recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        commentList = new ArrayList<>();
        getUserComments();
    }

    public void getUserComments() {
        // SET USER DATA MAP OBJECT:
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", "email");

        JsonArrayRequest jsonArrayRequest
                = new JsonArrayRequest(Request.Method.GET, ApiLinksHelper.getMyCommentsApiUri(), null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                recyclerView.setVisibility(View.VISIBLE);

                for (int i = 0; i < response.length(); i++) {
                    try {
                        // Получаем JSONArray, содержащий комментарий и имя пользователя
                        JSONArray commentArray = response.getJSONArray(i);
                        // Первый элемент массива - это объект комментария
                        JSONObject commentObject = commentArray.getJSONObject(0);
                        // Второй элемент массива - это имя пользователя
                        String username = commentArray.getString(1);

                        Comment comment = new Comment(commentObject.getString("content"));
                        // Здесь вы можете добавить имя пользователя к объекту комментария, если это необходимо
                         comment.setUsername(username);
                        commentList.add(comment);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CommentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
                adapter = new CommentListRecyclerViewHelper(commentList, CommentActivity.this);
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
                Toast.makeText(CommentActivity.this, "Не удалось получить список курсов", Toast.LENGTH_LONG).show();
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


}