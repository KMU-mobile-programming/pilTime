package com.example.piltime;
// PostActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        EditText postTitleEditText = findViewById(R.id.postTitleEditText);
        EditText postContentEditText = findViewById(R.id.postContentEditText);
        Button submitPostButton = findViewById(R.id.submitPostButton);

        submitPostButton.setOnClickListener(v -> {
            String postTitle = postTitleEditText.getText().toString();
            String postContent = postContentEditText.getText().toString();

            // 결과 전달을 위한 Intent에 게시글 제목과 내용 추가
            Intent resultIntent = new Intent();
            resultIntent.putExtra("postTitle", postTitle);
            resultIntent.putExtra("postContent", postContent);
            setResult(RESULT_OK, resultIntent); // 결과 전달
            finish(); // PostActivity 종료 후 CommunityActivity로 복귀
        });
    }
}