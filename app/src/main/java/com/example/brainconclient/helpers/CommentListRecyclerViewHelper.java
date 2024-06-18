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

import com.example.brainconclient.CommentActivity;
import com.example.brainconclient.R;
import com.example.brainconclient.TestDetailActivity;
import com.example.brainconclient.models.Comment;

import java.util.List;

public class CommentListRecyclerViewHelper extends RecyclerView.Adapter<CommentListRecyclerViewHelper.CommentListViewHolder> {
    private List<Comment> commentListItems;
    private Context context;

    public CommentListRecyclerViewHelper(List<Comment> commentListItems, Context context) {
        this.commentListItems = commentListItems;
        this.context = context;
    }
    // END OF NOTE LIST RECYCLER VIEW HELPER CLASS CONSTRUCTOR.

    @NonNull
    @Override
    public CommentListRecyclerViewHelper.CommentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list, parent, false);
        return new CommentListRecyclerViewHelper.CommentListViewHolder(v);
    }
    // END OF ON CREATE VIEW HOLDER METHOD.

    @Override
    public void onBindViewHolder(@NonNull CommentListRecyclerViewHelper.CommentListViewHolder holder, int position) {
        Comment comment = this.commentListItems.get(position);

        holder.content.setText(comment.getContent());
        holder.username.setText(comment.getUsername());

        holder.commentItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("comment_id", String.valueOf(comment.getComment_id()));
                intent.putExtra("content", comment.getContent());
                context.startActivity(intent);
            }
        });
    }
    // END OF ON BIND VIEW HOLDER METHOD.

    @Override
    public int getItemCount() {
        return commentListItems.size();
    }
    // END OF GET COUNT ITEM METHOD.

    public class CommentListViewHolder extends RecyclerView.ViewHolder {
        public TextView commentId, content, username;
        private LinearLayout commentItemLayout;

        public CommentListViewHolder(@NonNull View itemView) {
            super(itemView);
//            noteId      = itemView.findViewById(R.id.note_id);
            username = itemView.findViewById(R.id.user_name);
            content = itemView.findViewById(R.id.content);
            commentItemLayout = itemView.findViewById(R.id.commentItemLayout);

        }
        // END OF NOTE LIST VIEW HOLDER CONSTRUCTOR.
    }
}
