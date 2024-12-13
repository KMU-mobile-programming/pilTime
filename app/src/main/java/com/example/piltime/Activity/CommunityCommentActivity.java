// CommunityCommentActivity.java

package com.example.piltime.Activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.piltime.Database.DataBase;
import com.example.piltime.R;
import java.util.ArrayList;

public class CommunityCommentActivity extends AppCompatActivity {

    private ArrayList<String> commentList;
    private CommentAdapter commentAdapter;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "guest");
        DataBase dbHelper = new DataBase(this);
        userName = dbHelper.getNickById(userId);

        if (userName == null || userName.isEmpty()) {
            userName = "guest";
        }

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList, userName);

        Button commentButton = findViewById(R.id.postCommentButton);
        commentButton.setOnClickListener(v -> {
            if (userId.equals("guest")) {
                Toast.makeText(this, "게스트는 댓글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                openCommentDialog();
                }
        });
    }

    private void openCommentDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);

        EditText commentEditText = dialogView.findViewById(R.id.commentEditText);
        ListView commentListView = dialogView.findViewById(R.id.commentListView);
        commentListView.setAdapter(commentAdapter);

        Button postCommentButton = dialogView.findViewById(R.id.postCommentButton);
        postCommentButton.setOnClickListener(v -> {
            String comment = commentEditText.getText().toString();
            if (!comment.isEmpty()) {
                commentList.add(userName + ": " + comment);
                commentAdapter.notifyDataSetChanged();
                commentEditText.setText("");
                Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("댓글 작성")
                .setView(dialogView)
                .setCancelable(true)
                .setPositiveButton("닫기", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}