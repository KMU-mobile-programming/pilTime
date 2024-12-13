// CommentAdapter.java

package com.example.piltime.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.piltime.R;
import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> commentList;
    private LayoutInflater inflater;
    private String currentUser;

    public CommentAdapter(Context context, ArrayList<String> commentList, String currentUser) {
        this.context = context;
        this.commentList = commentList;
        this.inflater = LayoutInflater.from(context);
        this.currentUser = currentUser;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_comment, parent, false);
        }

        String comment = commentList.get(position);
        String commentAuthor = comment.substring(0, comment.indexOf(":"));

        TextView commentTextView = convertView.findViewById(R.id.commentTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteCommentButton);

        commentTextView.setText(comment);

        if (commentAuthor.equals(currentUser)) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> {
                commentList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            });
        } else {
            deleteButton.setVisibility(View.GONE);
        }

        return convertView;
    }
}