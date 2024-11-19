package com.example.piltime.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CommunityActivity extends AppCompatActivity {

    private static final int POST_REQUEST_CODE = 1;
    private LinearLayout postContainer;

    // SharedPreferences 파일명과 키 설정
    private static final String PREF_NAME = "UserPillsPrefs";
    private static final String KEY_PILL_TITLE = "pillTitle";
    private static final String KEY_PILL_CONTENT = "pillContent";
    private static final String KEY_PILL_IMAGE_URI = "pillImageUri";
    private static final String KEY_PILL_RESOURCE_ID = "pillResourceId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        TextView userNameTextView = findViewById(R.id.userNameTextView);
        FloatingActionButton postButton = findViewById(R.id.postButton);
        postContainer = findViewById(R.id.postContainer);

        // Intent에서 사용자 이름 가져오기
        String userName = getIntent().getStringExtra("userName");
        if (userName != null && !userName.isEmpty()) {
            userNameTextView.setText("Welcome, " + userName);
        } else {
            userNameTextView.setText("Welcome, User");
        }

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

            // 갤러리 URI 또는 추천 제품 리소스 처리
            Uri imageUri = data.getParcelableExtra("imageUri");
            int selectedProductImage = data.getIntExtra("selectedProductImage", -1);

            // SharedPreferences 객체 가져오기
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // 유저 복용약 정보 저장
            editor.putString(KEY_PILL_TITLE, postTitle);
            editor.putString(KEY_PILL_CONTENT, postContent);
            if (imageUri != null) {
                editor.putString(KEY_PILL_IMAGE_URI, imageUri.toString());
            } else if (selectedProductImage != -1) {
                editor.putInt(KEY_PILL_RESOURCE_ID, selectedProductImage);
            }
            editor.apply();

            // 게시글 레이아웃 생성
            LinearLayout postLayout = new LinearLayout(this);
            postLayout.setOrientation(LinearLayout.VERTICAL);
            postLayout.setPadding(16, 16, 16, 16);
            postLayout.setBackgroundResource(android.R.color.darker_gray);
            postLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // 제목
            TextView titleTextView = new TextView(this);
            titleTextView.setText("제목: " + postTitle);
            titleTextView.setTextSize(18);
            titleTextView.setPadding(0, 0, 0, 8);

            // 내용
            TextView contentTextView = new TextView(this);
            contentTextView.setText("내용: " + postContent);
            contentTextView.setTextSize(16);

            // 이미지 추가
            if (imageUri != null) {
                ImageView postImageView = new ImageView(this);
                postImageView.setImageURI(Uri.parse(sharedPreferences.getString(KEY_PILL_IMAGE_URI, "")));
                postLayout.addView(postImageView);
            } else if (sharedPreferences.contains(KEY_PILL_RESOURCE_ID)) {
                ImageView postImageView = new ImageView(this);
                int resourceId = sharedPreferences.getInt(KEY_PILL_RESOURCE_ID, -1);
                if (resourceId != -1) {
                    postImageView.setImageResource(resourceId);
                    postLayout.addView(postImageView);
                }
            }

            // 레이아웃에 제목, 내용 추가
            postLayout.addView(titleTextView);
            postLayout.addView(contentTextView);

            // 게시글을 컨테이너에 추가
            postContainer.addView(postLayout, 0);
        }
    }
}
