package com.example.brainconclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.brainconclient.helpers.StringResourceHelper;
import com.example.brainconclient.utils.MyVolleySingletonUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton createNoteBtn;

    private SharedPreferences preferences;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RecyclerView.Adapter adapter;
    TextView displayUsername, txtNoNotes;

    private RequestQueue mRequestQueue;

    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);

        displayUsername = findViewById(R.id.display_username);
        txtNoNotes = findViewById(R.id.no_notes);
        createNoteBtn = findViewById(R.id.create_note_btn);
        recyclerView = findViewById(R.id.note_list_recycler_view);
        progressBar = findViewById(R.id.get_not_progress_bar);

        mRequestQueue = MyVolleySingletonUtil.getInstance(MainActivity.this).getRequestQueue();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        setDisplayUsername();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
    }


    public void setDisplayUsername() {
        String username
                = preferences.getString("first_name", "") + " " + preferences.getString("last_name", "");
        displayUsername.setText("Welcome: " + username);
    }


    public void goToProfileActivity() {
        Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(goToProfile);
        //finish();
    }

}