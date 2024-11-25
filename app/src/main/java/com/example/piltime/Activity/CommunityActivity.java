package com.example.piltime.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.piltime.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity {

    private static final int POST_REQUEST_CODE = 1;
    private LinearLayout postContainer;

    // 댓글 리스트와 어댑터 초기화
    private ArrayList<String> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;

    private TextView userNameTextView;
    private String userName;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        postContainer = findViewById(R.id.postContainer);
        FloatingActionButton postButton = findViewById(R.id.postButton);

        // 게시글 작성 화면으로 이동
        postButton.setOnClickListener(v -> {
            Intent postIntent = new Intent(CommunityActivity.this, PostActivity.class);
            startActivityForResult(postIntent, POST_REQUEST_CODE);
        });

        // 유저 이름을 표시할 TextView 찾기
        userNameTextView = findViewById(R.id.userNameTextView);

        // MainActivity에서 전달된 Intent 데이터 받기
        Intent intent = getIntent();

        // userName이 전달되지 않았으면 기본값 "Guest" 설정
        userName = intent.getStringExtra("userName");
        if (userName == null || userName.isEmpty()) {
            userName = "Welcome, Guest";  // 기본값 설정
        }

        userId = intent.getStringExtra("userId");      // 전달된 userId 받기

        // 유저 이름을 TextView에 설정
        userNameTextView.setText(userName);
    }

    // 댓글 작성 및 댓글 리스트를 띄우는 팝업 다이얼로그
    private void openCommentDialog() {
        // 다이얼로그 레이아웃을 인플레이트
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);

        EditText commentEditText = dialogView.findViewById(R.id.commentEditText);
        ListView commentListView = dialogView.findViewById(R.id.commentListView);

        // 어댑터 초기화 (필요한 경우)
        commentAdapter = new CommentAdapter(this, commentList);
        commentListView.setAdapter(commentAdapter);

        // 댓글 작성 버튼
        Button postCommentButton = dialogView.findViewById(R.id.postCommentButton);
        postCommentButton.setOnClickListener(v -> {
            String comment = commentEditText.getText().toString();
            if (!comment.isEmpty()) {
                // 댓글 추가
                commentList.add(comment);
                commentAdapter.notifyDataSetChanged();  // 리스트 갱신
                commentEditText.setText("");  // 입력란 초기화
                Toast.makeText(CommunityActivity.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CommunityActivity.this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 다이얼로그 빌더 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("댓글 작성")
                .setView(dialogView)
                .setCancelable(true)  // 배경을 터치하면 다이얼로그 종료 가능
                .setPositiveButton("닫기", (dialog, which) -> dialog.dismiss());  // 닫기 버튼

        // 다이얼로그 표시
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String postTitle = data.getStringExtra("postTitle");
            String postContent = data.getStringExtra("postContent");
            String userName = data.getStringExtra("userName");
            String imageUriString = data.getStringExtra("imageUri");

            // 게시글 카드 생성
            CardView postCardView = new CardView(this);
            postCardView.setCardElevation(10f);
            postCardView.setRadius(16f);
            postCardView.setContentPadding(24, 24, 24, 24);
            // 레이아웃 파라미터 설정
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            // Margin 설정 (여백을 16dp로 설정)
            int margin = (int) getResources().getDimension(R.dimen.card_margin); // dp -> px 변환
            layoutParams.setMargins(0, 0, 0, 20);  // 좌, 상, 우, 하 여백 설정

            postCardView.setLayoutParams(layoutParams);

            // 게시글 레이아웃
            LinearLayout postLayout = new LinearLayout(this);
            postLayout.setOrientation(LinearLayout.VERTICAL);
            postLayout.setBackgroundColor(Color.WHITE);

            // 1. 유저 프로필 정보 (상단 왼쪽)
            LinearLayout userInfoLayout = new LinearLayout(this);
            userInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
            userInfoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            userInfoLayout.setPadding(16, 16, 16, 16);

            // 프로필 이미지
            ImageView userIcon = new ImageView(this);
            userIcon.setImageResource(R.drawable.ic_user_icon); // 사용자 아이콘
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(120, 120); // 고정 크기
            iconParams.setMargins(0, 0, 16, 0); // 오른쪽 여백
            userIcon.setLayoutParams(iconParams);
            userIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            userInfoLayout.addView(userIcon);

            // 유저 이름
            TextView userNameTextView = new TextView(this);
            userNameTextView.setText(userName);
            userNameTextView.setTextSize(16);
            userNameTextView.setTextColor(Color.BLACK);
            userNameTextView.setTypeface(null, Typeface.BOLD);
            userInfoLayout.addView(userNameTextView);

            // 유저 정보를 게시글 레이아웃에 추가
            postLayout.addView(userInfoLayout);

            // 2. 이미지 추가 (게시글 본문)
            if (imageUriString != null) {
                Uri imageUri = Uri.parse(imageUriString);
                ImageView postImageView = new ImageView(this);

                // 이미지 스타일 설정
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        800, // 가로 크기 고정
                        800  // 세로 크기 고정
                );
                imageParams.gravity = Gravity.CENTER; // 가운데 정렬
                imageParams.setMargins(0, 16, 0, 16); // 위아래 여백
                postImageView.setLayoutParams(imageParams);
                postImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                postImageView.setImageURI(imageUri);

                postLayout.addView(postImageView);
            }

            // 3. 게시글 제목 추가
            TextView titleTextView = new TextView(this);
            titleTextView.setText(postTitle);
            titleTextView.setTextSize(18);
            titleTextView.setTextColor(Color.BLACK);
            titleTextView.setTypeface(null, Typeface.BOLD);
            titleTextView.setPadding(16, 16, 16, 0);
            postLayout.addView(titleTextView);

            // 4. 게시글 내용 추가
            TextView contentTextView = new TextView(this);
            contentTextView.setText(postContent);
            contentTextView.setTextSize(14);
            contentTextView.setTextColor(Color.GRAY);
            contentTextView.setPadding(16, 8, 16, 16);
            postLayout.addView(contentTextView);

            // 5. 좋아요, 댓글, 스크랩 버튼 레이아웃
            LinearLayout buttonLayout = new LinearLayout(this);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            buttonLayout.setPadding(16, 8, 16, 16);

            // 좋아요 버튼
            Button likeButton = new Button(this);
            likeButton.setText("♡");
            likeButton.setBackgroundColor(Color.TRANSPARENT);
            likeButton.setTextSize(27);
            likeButton.setTextColor(Color.RED);

            // 클릭 시 하트 상태 변경
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 하트 모양을 변경
                    if (likeButton.getText().toString().equals("♡")) {
                        likeButton.setText("❤️");  // 꽉 찬 하트로 변경
                        likeButton.setTextSize(22);
                    } else {
                        likeButton.setTextSize(27);
                        likeButton.setText("♡");  // 빈 하트로 변경

                    }
                }
            });


            buttonLayout.addView(likeButton);

            // 댓글 버튼
            Button commentButton = new Button(this);
            commentButton.setText("💬");
            commentButton.setBackgroundColor(Color.TRANSPARENT);
            commentButton.setTextSize(24);
            commentButton.setTextColor(Color.BLUE);
            commentButton.setOnClickListener(v -> openCommentDialog());
            buttonLayout.addView(commentButton);

            // 스크랩 버튼
            Button saveButton = new Button(this);
            saveButton.setText("📌");
            saveButton.setBackgroundColor(Color.TRANSPARENT);
            saveButton.setTextSize(24);
            saveButton.setTextColor(Color.GREEN);

// 클릭 리스너 설정
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CommunityActivity.this, "스크랩 되었습니다!", Toast.LENGTH_SHORT).show();
                }
            });

            buttonLayout.addView(saveButton);


            // 버튼 레이아웃을 게시글 레이아웃에 추가
            postLayout.addView(buttonLayout);

            // 6. 게시글 레이아웃을 카드 뷰에 추가
            postCardView.addView(postLayout);

            // 게시글을 컨테이너에 추가
            postContainer.addView(postCardView, 0);
        }
    }


}
