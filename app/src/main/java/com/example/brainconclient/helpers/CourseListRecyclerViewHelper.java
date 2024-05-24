package com.example.brainconclient.helpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brainconclient.CourseDetailActivity;
import com.example.brainconclient.R;
import com.example.brainconclient.models.Course;

import java.util.List;

public class CourseListRecyclerViewHelper extends RecyclerView.Adapter<CourseListRecyclerViewHelper.NoteListViewHolder> {

    private List<Course> courseListItems;
    private Context context;

    public CourseListRecyclerViewHelper(List<Course> noteListItems, Context context){
        this.courseListItems = noteListItems;
        this.context = context;
    }
    // END OF NOTE LIST RECYCLER VIEW HELPER CLASS CONSTRUCTOR.

    @NonNull
    @Override
    public NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_list, parent, false);
        return new NoteListViewHolder(v);
    }
    // END OF ON CREATE VIEW HOLDER METHOD.

    @Override
    public void onBindViewHolder(@NonNull NoteListViewHolder holder, int position) {
        Course note = this.courseListItems.get(position);

//        holder.noteId.setText(String.valueOf(note.getNote_id()));
        holder.noteTitle.setText(note.getTitle());
        holder.noteBody.setText(note.getBody());


        holder.noteItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "You clicked: " + note.getNote_id(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("note_id", String.valueOf(note.getNote_id()));
                intent.putExtra("note_tile", note.getTitle());
                intent.putExtra("note_body", note.getBody());
                context.startActivity(intent);
            }
        });

    }
    // END OF ON BIND VIEW HOLDER METHOD.

    @Override
    public int getItemCount() {
        return courseListItems.size();
    }
    // END OF GET COUNT ITEM METHOD.

    public class NoteListViewHolder extends RecyclerView.ViewHolder{
        public TextView noteId, noteTitle, noteBody;
        private LinearLayout noteItemLayout;
        public NoteListViewHolder(@NonNull View itemView) {
            super(itemView);
//            noteId      = itemView.findViewById(R.id.note_id);
            noteTitle   = itemView.findViewById(R.id.note_title);
            noteBody    = itemView.findViewById(R.id.note_body);
            noteItemLayout = itemView.findViewById(R.id.noteItemLayout);
        }
        // END OF NOTE LIST VIEW HOLDER CONSTRUCTOR.
    }
    // END OF NOTE LIST VIEW HOLDER CLASS.
}
// END OF NOTE LIST RECYCLER VIEW HELPER CLASS.
