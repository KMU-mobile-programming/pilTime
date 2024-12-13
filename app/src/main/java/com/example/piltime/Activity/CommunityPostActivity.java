// CommunityPostActivity.java

package com.example.piltime.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.Database.DataBase;
import com.example.piltime.R;

import java.util.List;

public class CommunityPostActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView postImageView;
    private FrameLayout imageContainer;
    private Uri selectedImageUri;
    private LinearLayout medicineListLayout;
    private EditText postTitleEditText;
    private EditText postContentEditText;
    private String userName;
    private boolean isEditing = false;
    private int postPosition = -1;
    private String existingImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Intent로부터 사용자 정보 받기
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "guest"; // 기본값 설정
        }
        DataBase dbHelper = new DataBase(this);
        userName = dbHelper.getNickById(userId);


        if (userName == null || userName.isEmpty() || "guest".equals(userName)) {
            Toast.makeText(this, "게스트는 글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupListeners();
        handleEditMode();
        loadMedicineList(userId, dbHelper);
    }

    private void initializeViews() {
        postTitleEditText = findViewById(R.id.postTitleEditText);
        postContentEditText = findViewById(R.id.postContentEditText);
        postImageView = findViewById(R.id.postImageView);
        imageContainer = findViewById(R.id.imageContainer);
        medicineListLayout = findViewById(R.id.medicineListLayout);
    }

    private void handleEditMode() {
        Intent intent = getIntent();
        isEditing = intent.getBooleanExtra("isEditing", false);

        if (isEditing) {
            postPosition = intent.getIntExtra("postPosition", -1);
            // postId 추가
            Log.d("CommunityPostActivity", "Editing Post ID: " + postPosition); // 디버깅 로그 추가

            postTitleEditText.setText(intent.getStringExtra("postTitle"));
            postContentEditText.setText(intent.getStringExtra("postContent"));

            String imageUriString = intent.getStringExtra("imageUri");
            if (imageUriString != null) {
                selectedImageUri = Uri.parse(imageUriString);
                if (selectedImageUri != null) {
                    existingImagePath = imageUriString;
                    postImageView.setImageURI(selectedImageUri);
                    postImageView.setVisibility(View.VISIBLE);
                    findViewById(R.id.selectImageButton).setVisibility(View.GONE);
                } else {
                    Log.e("CommunityPostActivity", "Invalid image URI: " + imageUriString);
                }
            }
        }
    }



    private void setupListeners() {
        imageContainer.setOnClickListener(v -> openGallery());

        Button submitPostButton = findViewById(R.id.submitPostButton);
        submitPostButton.setText(isEditing ? "수정하기" : "작성하기");
        submitPostButton.setOnClickListener(v -> submitPost());

        ImageButton selectImageButton = findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(v -> openGallery());
    }

    private void loadMedicineList(String userId, DataBase dbHelper) {
        List<String> medicineNames = dbHelper.getMedicinesByUserId(userId);
        for (String medicineName : medicineNames) {
            Log.d("PostActivity", "Loaded medicine: " + medicineName);
            addMedicineItem(medicineName);
        }
    }

    private void addMedicineItem(String medicineName) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.medicine_item_layout, medicineListLayout, false);
        TextView textView = itemView.findViewById(R.id.medicineNameTextView);
        ImageView imageView = itemView.findViewById(R.id.medicineImageView);

        textView.setText(medicineName);
        imageView.setImageResource(R.drawable.sample_product_icon);

        itemView.setOnClickListener(v -> {
            selectedImageUri = Uri.parse("android.resource://com.example.piltime/" + R.drawable.sample_product_icon);
            postImageView.setImageURI(selectedImageUri);
            postImageView.setVisibility(View.VISIBLE);
            findViewById(R.id.selectImageButton).setVisibility(View.GONE);
        });

        medicineListLayout.addView(itemView);
    }

    private void submitPost() {
        String postTitle = postTitleEditText.getText().toString();
        String postContent = postContentEditText.getText().toString();

        if (postTitle.isEmpty() || postContent.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("postTitle", postTitle);
        resultIntent.putExtra("postContent", postContent);
        resultIntent.putExtra("userName", userName);

        // 이미지 저장 로직
        if (isEditing) {
            // 수정 모드일 경우 기존 이미지 경로를 사용
            resultIntent.putExtra("postId", postPosition);
            resultIntent.putExtra("imageUri", existingImagePath);
            resultIntent.putExtra("postPosition", postPosition);
            Log.d("CommunityPostActivity",postPosition + " 아이디 이거 아님?");
        }else if (selectedImageUri != null) {
            String imageUri = com.example.piltime.Activity.ImageSaveUtils.saveImage(this, selectedImageUri);
            if (imageUri != null) {
                resultIntent.putExtra("imageUri", imageUri);
            } else {
                // 이미지 저장 실패 시 기존 URI를 사용
                Toast.makeText(this, "이미지 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e("CommunityPostActivity", "Image save failed for URI: " + selectedImageUri.toString());
                return;
            }
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            postImageView.setImageURI(selectedImageUri);
            postImageView.setVisibility(View.VISIBLE);
            findViewById(R.id.selectImageButton).setVisibility(View.GONE);
        }
        long postId = data.getLongExtra("postId", -1);
        Log.d("CommunityPostActivity", postId + "담겨는 있는거냐");
    }
}