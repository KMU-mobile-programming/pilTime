package com.example.piltime.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.example.piltime.Database.DataBase;
import com.example.piltime.Database.Post;
import com.example.piltime.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private static final int POST_REQUEST_CODE = 1;
    private static final int EDIT_POST_REQUEST_CODE = 2;
    private LinearLayout postContainer;
    private ArrayList<String> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private TextView userNameTextView;
    private String userName;
    private String userId;
    private DataBase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        postContainer = findViewById(R.id.postContainer);
        FloatingActionButton postButton = findViewById(R.id.postButton);

        // DB 초기화
        dbHelper = new DataBase(this);

        // Intent로 userId 값 가져오기
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "guest";
            Log.d("CommunityActivity", "No userId found, setting as guest");
        } else {
            Log.d("CommunityActivity", "Logged in as: " + userId);
        }

// 사용자 닉네임 가져오기
        userName = dbHelper.getNickById(userId);
        if (userName == null) {
            userName = "게스트";
        }
        Log.d("CommunityActivity", "User nickname: " + userName);

// 게스트 체크 수정 - 실제 게스트 계정인 경우에만 제한
        if ("guest".equals(userId)) {
            postButton.setVisibility(View.GONE);
        } else {
            postButton.setVisibility(View.VISIBLE);
            postButton.setOnClickListener(v -> {
                Intent postIntent = new Intent(CommunityActivity.this, CommunityPostActivity.class);
                postIntent.putExtra("userId", userId);
                startActivityForResult(postIntent, POST_REQUEST_CODE);
            });
        }

        // 상단에 사용자 닉네임 표시
        userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView.setText(userName + "님 환영합니다!");

        // 게시글 로드
        loadPosts();
    }

    private void loadPosts() {
        // 기존 게시글 모두 제거
        postContainer.removeAllViews();
        // DB에서 모든 게시글 가져오기
        List<Post> posts = dbHelper.getAllPosts();
        // 게시글을 최신순으로 표시
        for (Post post : posts) {
            addPostView(post);
        }
    }
    private boolean isGuestUser() {
        return "guest".equals(userId);
    }
    private boolean isUserOwner(String postAuthorId) {
        Log.d("CommunityActivity", "Checking ownership - PostAuthor: " + postAuthorId + ", CurrentUser: " + userId);
        return userId != null && userId.equals(postAuthorId) && !"guest".equals(userId);
    }

    private void openCommentDialog() {
        if ("guest".equals(userId)) {
            Toast.makeText(this, "게스트는 댓글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);

        EditText commentEditText = dialogView.findViewById(R.id.commentEditText);
        ListView commentListView = dialogView.findViewById(R.id.commentListView);

        commentAdapter = new CommentAdapter(this, commentList, userName);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("postTitle");
            String content = data.getStringExtra("postContent");
            String imageUri = data.getStringExtra("imageUri");

            Log.d("ActivityResult", "RequestCode: " + requestCode);
            Log.d("ActivityResult", "Title: " + title);
            Log.d("ActivityResult", "Content: " + content);

            if (requestCode == POST_REQUEST_CODE) {
                // 새 게시글 작성
                long postId = dbHelper.addPost(userId, title, content, imageUri);
                if (postId != -1) {
                    Toast.makeText(this, "게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                    loadPosts();
                } else {
                    Toast.makeText(this, "게시글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == EDIT_POST_REQUEST_CODE) {
                // 게시글 수정
                long postId = data.getIntExtra("postId", -1);
                Log.d("CommunityPostActivity", "Edit Mode - PostId: " + postId);

                if (postId != -1) {
                    boolean updateSuccess = dbHelper.updatePost(postId, title, content, imageUri);
                    Log.d("ActivityResult", "Update Result: " + updateSuccess);

                    if (updateSuccess) {
                        Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        loadPosts(); // 목록 새로고침
                    } else {
                        Toast.makeText(this, "게시글 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("ActivityResult", "Invalid postId received");
                    Toast.makeText(this, "게시글 수정에 실패했습니다: 잘못된 게시글 ID", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Log.d("ActivityResult", "No data received or result not OK");
        }
    }

    private CardView createPostCard(String postTitle, String postContent, String authorId, String imageUriString, Post post) {
        CardView postCardView = new CardView(this);
        postCardView.setCardElevation(10f);
        postCardView.setRadius(16f);
        postCardView.setContentPadding(24, 24, 24, 24);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 20);
        postCardView.setLayoutParams(layoutParams);

        LinearLayout postLayout = new LinearLayout(this);
        postLayout.setOrientation(LinearLayout.VERTICAL);
        postLayout.setBackgroundColor(Color.WHITE);

        // 사용자 정보 레이아웃 추가
        LinearLayout userInfoLayout = new LinearLayout(this);
        userInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
        userInfoLayout.setPadding(16, 16, 16, 16);

        ImageView userIcon = new ImageView(this);
        userIcon.setImageResource(R.drawable.ic_user_icon);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(120, 120);
        iconParams.setMargins(0, 0, 16, 0);
        userIcon.setLayoutParams(iconParams);
        userIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        userInfoLayout.addView(userIcon);

        TextView userNameView = new TextView(this);
        String authorName = dbHelper.getNickById(authorId);
        userNameView.setText(authorName != null ? authorName : "Unknown User");
        userNameView.setTextSize(16);
        userNameView.setTextColor(Color.BLACK);
        userNameView.setTypeface(null, Typeface.BOLD);
        userInfoLayout.addView(userNameView);

        postLayout.addView(userInfoLayout);

        // 이미지 추가
        if (imageUriString != null) {
            ImageView postImageView = new ImageView(this);
            Uri imageUri = Uri.parse(imageUriString);
            postImageView.setImageURI(imageUri);
            postImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(800, 800);
            imageParams.gravity = Gravity.CENTER;
            imageParams.setMargins(0, 16, 0, 16);
            postImageView.setLayoutParams(imageParams);
            postLayout.addView(postImageView);
        }

        // 제목 추가
        TextView titleView = new TextView(this);
        titleView.setText(postTitle);
        titleView.setTextSize(18);
        titleView.setTextColor(Color.BLACK);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setPadding(16, 16, 16, 0);
        postLayout.addView(titleView);

        // 내용 추가
        TextView contentView = new TextView(this);
        contentView.setText(postContent);
        contentView.setTextSize(14);
        contentView.setTextColor(Color.GRAY);
        contentView.setPadding(16, 8, 16, 16);
        postLayout.addView(contentView);

        // 버튼 레이아웃
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        buttonLayout.setPadding(4, 4, 4, 16);
        //버튼들의 공통 레이아웃 파라미터 정의
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(4, 0, 4, 0);
        // 좋아요 버튼
        Button likeButton = new Button(this);
        likeButton.setLayoutParams(new LinearLayout.LayoutParams(buttonParams));
        likeButton.setText("♡");
        likeButton.setBackgroundColor(Color.TRANSPARENT);
        likeButton.setTextSize(27);
        likeButton.setTextColor(Color.RED);
        likeButton.setOnClickListener(v -> {
            if (isGuestUser()) {
                Toast.makeText(this, "게스트는 좋아요를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (likeButton.getText().toString().equals("♡")) {
                likeButton.setText("❤️");
                likeButton.setTextSize(22);
            } else {
                likeButton.setTextSize(27);
                likeButton.setText("♡");
            }
        });
        buttonLayout.addView(likeButton);

        // 댓글 버튼
        Button commentButton = new Button(this);
        commentButton.setLayoutParams(new LinearLayout.LayoutParams(buttonParams));
        commentButton.setText("💬");
        commentButton.setBackgroundColor(Color.TRANSPARENT);
        commentButton.setTextSize(24);
        commentButton.setTextColor(Color.BLUE);
        // 댓글 버튼
        commentButton.setOnClickListener(v -> {
            if ("guest".equals(userId)) {
                Toast.makeText(this, "게스트는 댓글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            openCommentDialog();
        });
        buttonLayout.addView(commentButton);


        // 스크랩 버튼
        Button saveButton = new Button(this);
        saveButton.setLayoutParams(new LinearLayout.LayoutParams(buttonParams));
        saveButton.setText("📌");
        saveButton.setBackgroundColor(Color.TRANSPARENT);
        saveButton.setTextSize(24);
        saveButton.setTextColor(Color.GREEN);
        // 스크랩 버튼
        saveButton.setOnClickListener(v -> {
            if (isGuestUser()) {
                Toast.makeText(this, "게스트는 스크랩을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "스크랩 되었습니다!", Toast.LENGTH_SHORT).show();
        });
        buttonLayout.addView(saveButton);

        // 게시글 작성자인 경우 수정/삭제 버튼 추가
        if (isUserOwner(authorId)) {
            Log.d("CommunityActivity", "Adding edit/delete buttons for post author");

            // 수정 버튼
            Button editButton = new Button(this);
            editButton.setLayoutParams(new LinearLayout.LayoutParams(buttonParams));
            editButton.setText("수정");
            editButton.setBackgroundColor(Color.LTGRAY);
            editButton.setOnClickListener(v -> {
                Intent editIntent = new Intent(this, CommunityPostActivity.class);
                editIntent.putExtra("isEditing", true);
                editIntent.putExtra("postId",post.getId());
                editIntent.putExtra("postTitle", postTitle);
                editIntent.putExtra("postContent", postContent);
                editIntent.putExtra("imageUri", imageUriString);
                editIntent.putExtra("postPosition", postContainer.indexOfChild(postCardView));
                editIntent.putExtra("USER_ID", userId);
                startActivityForResult(editIntent, EDIT_POST_REQUEST_CODE);
            });
            buttonLayout.addView(editButton);
            // 삭제 버튼
            Button deleteButton = new Button(this);
            deleteButton.setLayoutParams(new LinearLayout.LayoutParams(buttonParams));
            deleteButton.setText("삭제");
            deleteButton.setBackgroundColor(Color.LTGRAY);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("게시글 삭제")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton("예", (dialog, which) -> {
                            if (dbHelper.deletePost(post.getId(), userId)) {
                                postContainer.removeView(postCardView);
                                Toast.makeText(this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                loadPosts();
                            } else {
                                Toast.makeText(this, "게시글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
            }); // setOnClickListener의 끝
            buttonLayout.addView(deleteButton);
        }
        postLayout.addView(buttonLayout);
        postCardView.addView(postLayout);
        return postCardView;
    }

    private void addPostView(Post post) {
        CardView postCardView = createPostCard(
                post.getTitle(),
                post.getContent(),
                post.getUserId(),
                post.getImageUri(),
                post
        );
        postContainer.addView(postCardView, 0);  // 최신 글이 위로 오도록
    }
}