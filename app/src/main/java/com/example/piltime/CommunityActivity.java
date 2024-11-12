package com.example.piltime;
// CommunityActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CommunityActivity extends AppCompatActivity {

    private static final int POST_REQUEST_CODE = 1;
    private LinearLayout postContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        TextView userNameTextView = findViewById(R.id.userNameTextView);
        Button postButton = findViewById(R.id.postButton);
        postContainer = findViewById(R.id.postContainer); // 게시글을 표시할 컨테이너

        // 게시글 작성 페이지로 이동
        postButton.setOnClickListener(v -> {
            Intent postIntent = new Intent(CommunityActivity.this, PostActivity.class);
            startActivityForResult(postIntent, POST_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String postTitle = data.getStringExtra("postTitle");
            String postContent = data.getStringExtra("postContent");

            // 게시글을 표시할 TextView 생성 및 설정
            TextView newPostView = new TextView(this);
            newPostView.setText("제목: " + postTitle + "\n내용: " + postContent);
            newPostView.setPadding(16, 16, 16, 16);
            newPostView.setBackgroundResource(android.R.color.darker_gray);
            newPostView.setTextSize(16);
            postContainer.addView(newPostView); // 게시글을 컨테이너에 추가
        }
    }
}
