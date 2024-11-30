package com.example.piltime.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.R;

import java.util.ArrayList;

public class CommunityCommentActivity extends AppCompatActivity {

    private ArrayList<String> commentList;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);

        // 댓글 작성 버튼 (아이템에 댓글 달기 버튼)
        Button commentButton = findViewById(R.id.postCommentButton);
        commentButton.setOnClickListener(v -> {
            // 댓글 작성 팝업 열기
            openCommentDialog();
        });
    }

    // 댓글 작성 및 댓글 리스트를 띄우는 팝업 다이얼로그
    private void openCommentDialog() {
        // 다이얼로그 레이아웃을 인플레이트
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);

        EditText commentEditText = dialogView.findViewById(R.id.commentEditText);
        ListView commentListView = dialogView.findViewById(R.id.commentListView);
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
                Toast.makeText(CommunityCommentActivity.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CommunityCommentActivity.this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
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
}
