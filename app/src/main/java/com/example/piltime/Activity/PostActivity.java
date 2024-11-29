package com.example.piltime.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.Database.AppDatabaseHelper;
import com.example.piltime.R;

import java.util.List;

public class PostActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView postImageView;
    private FrameLayout imageContainer;
    private Uri selectedImageUri;
    private LinearLayout medicineListLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        EditText postTitleEditText = findViewById(R.id.postTitleEditText);
        EditText postContentEditText = findViewById(R.id.postContentEditText);
        Button submitPostButton = findViewById(R.id.submitPostButton);
        postImageView = findViewById(R.id.postImageView);
        ImageButton selectImageButton = findViewById(R.id.selectImageButton);
        imageContainer = findViewById(R.id.imageContainer);
        medicineListLayout = findViewById(R.id.medicineListLayout);

        // AppDatabaseHelper 초기화
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);

        // 갤러리 열기 설정
        imageContainer.setOnClickListener(v -> openGallery());

        // 게시글 제출 버튼 동작
        submitPostButton.setOnClickListener(v -> {
            String postTitle = postTitleEditText.getText().toString();
            String postContent = postContentEditText.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("postTitle", postTitle);
            resultIntent.putExtra("postContent", postContent);

            // 이미지 URI가 있다면 Intent에 추가
            if (selectedImageUri != null) {
                resultIntent.putExtra("imageUri", selectedImageUri.toString());  // URI를 String으로 전달
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // 사용자 ID를 SharedPreferences에서 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        // 데이터베이스에서 복용약 목록 가져오기 (특정 사용자 약물)
        List<String> medicineNames = databaseHelper.getMedicinesByUserId(userId);
        for (String medicineName : medicineNames) {
            Log.d("PostActivity", "Loaded medicine: " + medicineName);
            addMedicineItem(medicineName);
        }
    }



    // 복용약 아이템을 카드 형태로 추가하는 메서드
    private void addMedicineItem(String medicineName) {
        // 카드 뷰 생성
        View itemView = LayoutInflater.from(this).inflate(R.layout.medicine_item_layout, medicineListLayout, false);

        TextView textView = itemView.findViewById(R.id.medicineNameTextView);
        ImageView imageView = itemView.findViewById(R.id.medicineImageView);

        // 약 이름을 텍스트뷰에 설정
        textView.setText(medicineName);
        imageView.setImageResource(R.drawable.sample_product_icon);  // 샘플 아이콘 설정

        // 이미지 클릭 이벤트 추가
        itemView.setOnClickListener(v -> {
            // 클릭된 약의 이미지를 게시글 이미지에 올리기
            selectedImageUri = Uri.parse("android.resource://com.example.piltime/" + R.drawable.sample_product_icon);
            postImageView.setImageURI(selectedImageUri);  // 약 이미지로 변경
            postImageView.setVisibility(View.VISIBLE);  // 이미지를 표시
            findViewById(R.id.selectImageButton).setVisibility(View.GONE);  // 이미지 선택 버튼 숨기기
        });

        // 만들어진 카드 뷰를 리스트 레이아웃에 추가
        medicineListLayout.addView(itemView);
    }

    // 갤러리 열기 메서드
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    // 이미지 선택 후 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            postImageView.setImageURI(selectedImageUri);  // 선택한 이미지 URI 설정
            postImageView.setVisibility(View.VISIBLE);   // 이미지를 표시
            findViewById(R.id.selectImageButton).setVisibility(View.GONE);  // 이미지 선택 버튼 숨기기
        }
    }
}
