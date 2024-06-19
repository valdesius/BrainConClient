package com.example.brainconclient.helpers;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.brainconclient.CourseDetailActivity;
import com.example.brainconclient.CreateCourseActivity;
import com.example.brainconclient.R;
import com.example.brainconclient.models.Course;
import com.example.brainconclient.utils.MyVolleySingletonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseListRecyclerViewHelper extends RecyclerView.Adapter<CourseListRecyclerViewHelper.NoteListViewHolder> {
    private SharedPreferences preferences;
    private List<Course> courseListItems;
    private Context context;
    private RequestQueue requestQueue;

    public CourseListRecyclerViewHelper(List<Course> courseListItems, Context context){
        this.courseListItems = courseListItems;
        this.context = context;
        this.preferences = context.getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), Context.MODE_PRIVATE);
        this.requestQueue = MyVolleySingletonUtil.getInstance(context).getRequestQueue();
    }

    @NonNull
    @Override
    public NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_list, parent, false);

        return new NoteListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListViewHolder holder, int position) {
        Course course = this.courseListItems.get(position);
        // Загрузка статуса избранного из SharedPreferences
        boolean isFavorite = preferences.getBoolean("favorite_" + course.getNote_id(), false);
        course.setFavorite(isFavorite); // Обновление статуса избранного в объекте курса
        boolean isGuest = preferences.getBoolean("isGuest", false);

        // Установка текста заголовка и описания курса
        holder.noteTitle.setText(course.getTitle());
        holder.noteBody.setText(course.getBody());

        // Установка изображения сердца в зависимости от статуса избранного
        holder.favoriteButton.setImageResource(isFavorite ? R.drawable.hearred : R.drawable.heart);

        // Обработчик нажатия на элемент списка курсов
        if (!isGuest) {
        holder.noteItemLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("note_id", String.valueOf(course.getNote_id()));
            intent.putExtra("note_title", course.getTitle());
            intent.putExtra("note_body", course.getBody());
            context.startActivity(intent);
        });

        // Обработчик нажатия на иконку сердца
        holder.favoriteButton.setOnClickListener(v -> {
            // Изменение статуса избранного и сохранение в SharedPreferences
            boolean newFavStatus = !isFavorite;
            course.setFavorite(newFavStatus);
            preferences.edit().putBoolean("favorite_" + course.getNote_id(), newFavStatus).apply();

            updateFavoriteStatusOnServer(course.getNote_id(), newFavStatus);
            notifyItemChanged(position);
        });}
        else {
            // Если пользователь является гостем, убираем все обработчики нажатия
            holder.noteItemLayout.setOnClickListener(null);
            holder.favoriteButton.setOnClickListener(null);
        }
    }

    private void updateFavoriteStatusOnServer(int noteId, boolean newStatus) {
        String url = ApiLinksHelper.updateFavorite(noteId, newStatus); // URL должен включать user_id

        StringRequest request = new StringRequest(Request.Method.PUT, url, response -> {
            // Обработка успешного ответа сервера
            Log.i("CourseListRecyclerViewHelper", "Статус избранного обновлён!");
            Toast.makeText(context, "Favorite status updated", Toast.LENGTH_SHORT).show();
        }, error -> {
            // Обработка ошибки
            error.printStackTrace();
            Log.i("CourseListRecyclerViewHelper", "Ошибка при обновлении статуса избранного");
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("note_id", String.valueOf(noteId));
                params.put("is_favorite", newStatus ? "1" : "0");
                params.put("user_id", String.valueOf(preferences.getInt("user_id", -1))); // Добавьте user_id в параметры
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Убедитесь, что preferences не null
                if (preferences == null) {
                    throw new IllegalStateException("SharedPreferences not initialized");
                }
                String token = preferences.getString("token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return courseListItems.size();
    }

    public class NoteListViewHolder extends RecyclerView.ViewHolder{
        public TextView noteTitle, noteBody;
        public ImageView favoriteButton;
        private LinearLayout noteItemLayout;

        public NoteListViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle   = itemView.findViewById(R.id.note_title);
            noteBody    = itemView.findViewById(R.id.note_body);
            favoriteButton = itemView.findViewById(R.id.favorite_btn);
            noteItemLayout = itemView.findViewById(R.id.noteItemLayout);
        }
    }
}