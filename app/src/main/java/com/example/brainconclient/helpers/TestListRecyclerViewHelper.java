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
import com.example.brainconclient.models.Test;

import java.util.List;

public class TestListRecyclerViewHelper extends RecyclerView.Adapter<TestListRecyclerViewHelper.TestListViewHolder> {
    private List<Test> testListItems;
    private Context context;

    public TestListRecyclerViewHelper(List<Test> testListItems, Context context) {
        this.testListItems = testListItems;
        this.context = context;
    }
    // END OF NOTE LIST RECYCLER VIEW HELPER CLASS CONSTRUCTOR.

    @NonNull
    @Override
    public TestListRecyclerViewHelper.TestListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_list, parent, false);
        return new TestListRecyclerViewHelper.TestListViewHolder(v);
    }
    // END OF ON CREATE VIEW HOLDER METHOD.

    @Override
    public void onBindViewHolder(@NonNull TestListRecyclerViewHelper.TestListViewHolder holder, int position) {
        Test test = this.testListItems.get(position);

//        holder.noteId.setText(String.valueOf(note.getNote_id()));
        holder.testTitle.setText(test.getTitle());
        holder.testBody.setText(test.getBody());


        holder.testItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "You clicked: " + note.getNote_id(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("test_id", String.valueOf(test.getTest_id()));
                intent.putExtra("test_tile", test.getTitle());
                intent.putExtra("test_body", test.getBody());
                context.startActivity(intent);
            }
        });

    }
    // END OF ON BIND VIEW HOLDER METHOD.

    @Override
    public int getItemCount() {
        return testListItems.size();
    }
    // END OF GET COUNT ITEM METHOD.

    public class TestListViewHolder extends RecyclerView.ViewHolder {
        public TextView testId, testTitle, testBody;
        private LinearLayout testItemLayout;

        public TestListViewHolder(@NonNull View itemView) {
            super(itemView);
//            noteId      = itemView.findViewById(R.id.note_id);
            testTitle = itemView.findViewById(R.id.test_title);
            testBody = itemView.findViewById(R.id.test_body);
            testItemLayout = itemView.findViewById(R.id.testItemLayout);
        }
        // END OF NOTE LIST VIEW HOLDER CONSTRUCTOR.
    }
}
