package com.example.piltime;

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

import java.util.HashSet;
import java.util.Set;

public class PostActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_MEDICINE_LIST = "medicine_list";
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

        // 갤러리 열기 설정
        imageContainer.setOnClickListener(v -> openGallery());

        // 게시글 제출 버튼 동작
        submitPostButton.setOnClickListener(v -> {
            String postTitle = postTitleEditText.getText().toString();
            String postContent = postContentEditText.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("postTitle", postTitle);
            resultIntent.putExtra("postContent", postContent);

            if (selectedImageUri != null) {
                resultIntent.putExtra("imageUri", selectedImageUri);
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // SharedPreferences에서 복용약 데이터 로드
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> medicineNames = loadMedicineDataFromSharedPreferences(sharedPreferences);


        // 복용약 리스트 동적 생성
        for (String medicineName : medicineNames) {
            Log.d("PostActivity", "Loaded medicine: " + medicineName);
            addMedicineItem(medicineName);
        }
    }

    // SharedPreferences에서 데이터 로드
    private Set<String> loadMedicineDataFromSharedPreferences(SharedPreferences sharedPreferences) {
        return sharedPreferences.getStringSet(KEY_MEDICINE_LIST, new HashSet<>());
    }

    // 복용약 아이템 추가 메서드
    private void addMedicineItem(String medicineName) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.medicine_item_layout, medicineListLayout, false);

        TextView textView = itemView.findViewById(R.id.medicineNameTextView);
        ImageView imageView = itemView.findViewById(R.id.medicineImageView);

        textView.setText(medicineName);
        imageView.setImageResource(R.drawable.sample_product_icon);

        medicineListLayout.addView(itemView);
        medicineListLayout.invalidate();
    }

    // 갤러리 열기 메서드
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    // 이미지 선택 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            postImageView.setImageURI(selectedImageUri);
            postImageView.setVisibility(View.VISIBLE);

            findViewById(R.id.selectImageButton).setVisibility(View.GONE);
        }
    }
}
