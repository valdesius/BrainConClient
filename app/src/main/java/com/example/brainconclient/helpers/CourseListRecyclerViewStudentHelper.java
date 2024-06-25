package com.example.brainconclient.helpers;

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
import com.android.volley.toolbox.StringRequest;
import com.example.brainconclient.CourseDetailActivity;
import com.example.brainconclient.CourseDetailActivityStudent;
import com.example.brainconclient.R;
import com.example.brainconclient.models.Course;
import com.example.brainconclient.utils.MyVolleySingletonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseListRecyclerViewStudentHelper extends RecyclerView.Adapter<CourseListRecyclerViewStudentHelper.CourseListViewHolder> {
    private SharedPreferences preferences;
    private List<Course> courseListItems;
    private Context context;
    private RequestQueue requestQueue;

    public CourseListRecyclerViewStudentHelper(List<Course> courseListItems, Context context, boolean isGuest){
        this.courseListItems = courseListItems;
        this.context = context;
        this.preferences = context.getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), Context.MODE_PRIVATE);
        this.requestQueue = MyVolleySingletonUtil.getInstance(context).getRequestQueue();
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_list, parent, false);

        return new CourseListViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        Course course = this.courseListItems.get(position);
        boolean isFavorite = preferences.getBoolean("favorite_" + course.getCourse_id(), false);
        course.setFavorite(isFavorite);
        boolean isGuest = preferences.getBoolean("isGuest", false);

        holder.courseTitle.setText(course.getTitle());
        holder.courseBody.setText(course.getBody());
        holder.favoriteButton.setImageResource(isFavorite ? R.drawable.hearred : R.drawable.heart);

        if (!isGuest) {
            holder.courseItemLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, CourseDetailActivityStudent.class);
                intent.putExtra("course_id", String.valueOf(course.getCourse_id()));
                intent.putExtra("course_title", course.getTitle());
                intent.putExtra("course_body", course.getBody());
                context.startActivity(intent);
            });

            holder.favoriteButton.setOnClickListener(v -> {
                boolean newFavStatus = !isFavorite;
                course.setFavorite(newFavStatus);
                preferences.edit().putBoolean("favorite_" + course.getCourse_id(), newFavStatus).apply();
                updateFavoriteStatusOnServer(course.getCourse_id(), newFavStatus);
                notifyItemChanged(position);
            });
        } else {
            holder.courseItemLayout.setOnClickListener(null);
            holder.favoriteButton.setOnClickListener(null);
        }

    }

    private void updateFavoriteStatusOnServer(int courseId, boolean newStatus) {
        String url = ApiLinksHelper.updateFavorite(courseId, newStatus); // URL должен включать user_id

        StringRequest request = new StringRequest(Request.Method.PUT, url, response -> {
            // Обработка успешного ответа сервера
            Log.i("CourseListRecyclerViewHelperStudent", "Статус избранного обновлён!");
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
                params.put("course_id", String.valueOf(courseId));
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

    public class CourseListViewHolder extends RecyclerView.ViewHolder{
        public TextView courseTitle, courseBody;
        public ImageView favoriteButton;
        private LinearLayout courseItemLayout;

        public CourseListViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle   = itemView.findViewById(R.id.course_title);
            courseBody    = itemView.findViewById(R.id.course_body);
            favoriteButton = itemView.findViewById(R.id.favorite_btn);
            courseItemLayout = itemView.findViewById(R.id.courseItemLayout);
        }
    }
}