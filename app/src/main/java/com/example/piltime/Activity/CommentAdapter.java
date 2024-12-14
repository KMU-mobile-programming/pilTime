package com.example.piltime.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.piltime.R;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> commentList;
    private LayoutInflater inflater;

    public CommentAdapter(Context context, ArrayList<String> commentList) {
        this.context = context;
        this.commentList = commentList;
        this.inflater = LayoutInflater.from(context);
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

        TextView commentTextView = convertView.findViewById(R.id.commentTextView);
        commentTextView.setText(commentList.get(position));

        return convertView;
    }
}
