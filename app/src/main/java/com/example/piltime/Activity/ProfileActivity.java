package com.example.piltime.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.Database.DataBase;
import com.example.piltime.R;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    String mynickname ;
    int myage = 21;
    boolean amiboy = true;
    private String userId;

    int profilepic = R.drawable.ic_launcher_foreground;
    List<String> mypills;

    String[] myposttitles = new String[]{
            "처방약 2번 안먹었는데 괜찮은가요?",
            "피부 영양 공급에 좋은 영양제",
            "혈압약 1회 복용 빠뜨렸을 때 대처법",
            "감기약 복용 중 프로폴리스 같이 먹어도 될까요",
            "빈혈 개선에 효과적인 영양제 조합"};
    boolean[] mypostbest = new boolean[]{
            true, true, false, true, false
    };
    int[] mypostcom = new int[]{
            1, 69, 78, 34, 2
    };

    String[] mybmarktitles = new String[]{
            "머리카락 영양 공급을 위한 비오틴 선택 가이드",
            "비타민C 고용량, 매일 복용해도 괜찮을까?",
            "처방받은 약, 빈속에 먹어도 괜찮을까?",
            "눈 건강을 위한 비타민, 어떤 걸 고를까요",
            "장 건강 개선에 도움 되는 프로바이오틱스, 어떻게 먹어야 할까?"};
    boolean[] mybmarkbest = new boolean[]{
            false, false, false, true, false
    };
    int[] mybmarkcom = new int[]{
            5, 6, 78, 9, 1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
        profilePic.setImageDrawable(getDrawable(profilepic));

        // Intent로부터 사용자 ID 가져오기
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "guest";
            Log.d("ProfileActivity", "No userId found, setting as guest");
        } else {
            Log.d("ProfileActivity", "Logged in as: " + userId);
        }

        // DataBase 인스턴스를 생성하고 약 이름을 가져옵니다.
        DataBase dbHelper = new DataBase(this);
        mynickname = dbHelper.getNickById(userId);
        mypills = dbHelper.getMedicinesByUserId(userId); // 사용자 ID에 따른 약 이름을 가져옵니다.


    // 로그 출력
        Log.d("ProfileActivity", "Retrieved medicines:" + mypills);
        Log.d("DBHelper", "Fetched medicine names: " + mypills.toString());

        ImageView gotosettings = (ImageView) findViewById(R.id.profileSettings);
        gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });

        TextView profileNickname = (TextView) findViewById(R.id.profileNickname);
        profileNickname.setText(mynickname);
        TextView profilePersonal = (TextView) findViewById(R.id.profilePersonal);
        if (amiboy) {
            profilePersonal.setText(myage + " / 남");
        } else {
            profilePersonal.setText(myage + " / 여");
        }

        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(240, 240);
        imgParams.setMargins(20, 20, 20, 20);
        LinearLayout.LayoutParams postParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        postParams.setMargins(0, 12, 0, 12);
        LinearLayout.LayoutParams comParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout myPills = (LinearLayout) findViewById(R.id.myPills);

        // 기본 이미지 설정
        int defaultPillImg = R.drawable.sample_product_icon; // 기본 이미지 리소스

        for (int i = 0; i < mypills.size(); i++) {
            LinearLayout newPill = new LinearLayout(this);
            newPill.setOrientation(LinearLayout.VERTICAL);
            ImageView newPillImg = new ImageView(this);

            newPillImg.setImageDrawable(getDrawable(defaultPillImg)); // 기본 이미지 설정

            newPillImg.setBackground(getDrawable(R.drawable.background_rounded_corners));
            newPillImg.setClipToOutline(true);
            newPillImg.setLayoutParams(imgParams);

            TextView newPillTxt = new TextView(this);
            newPillTxt.setText(mypills.get(i));
            newPillTxt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            newPillTxt.setTextSize(12);
            newPillTxt.setTextColor(Color.WHITE);

            newPill.addView(newPillImg);
            newPill.addView(newPillTxt);
            myPills.addView(newPill);
        }

        LinearLayout myPosts = (LinearLayout) findViewById(R.id.myPosts);
        for (int i = 0; i < mypostbest.length; i++) {
            LinearLayout newPost = new LinearLayout(this);
            newPost.setOrientation(LinearLayout.HORIZONTAL);
            newPost.setBackground(getDrawable(R.drawable.rounded_984f4f));
            newPost.setLayoutParams(postParams);
            TextView newPostBest = new TextView(this);
            newPostBest.setPadding(48, 0, 0, 0);
            newPostBest.setTextColor(0xFFFFEE00);
            if (mypostbest[i]) {
                newPostBest.setText("★");
            }
            TextView newPostTitle = new TextView(this);
            newPostTitle.setPadding(24, 18, 0, 18);
            newPostTitle.setText(myposttitles[i]);
            newPostTitle.setTextColor(Color.WHITE);
            if (newPostTitle.getText().length() >= 16) {
                newPostTitle.setText(newPostTitle.getText().subSequence(0, 16) + "...");
            }
            TextView newPostCom = new TextView(this);
            newPostCom.setPadding(0, 0, 48, 0);
            newPostCom.setText("[" + mypostcom[i] + "]");
            newPostCom.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            newPostCom.setTextColor(0xFF91CAB1);
            newPostCom.setTypeface(newPostCom.getTypeface(), Typeface.BOLD);
            newPostCom.setLayoutParams(comParams);
            newPost.addView(newPostBest);
            newPost.addView(newPostTitle);
            newPost.addView(newPostCom);
            myPosts.addView(newPost);
        }

        LinearLayout myBmarks = (LinearLayout) findViewById(R.id.myBmarks);
        for (int i = 0; i < mybmarkbest.length; i++) {
            LinearLayout newPost = new LinearLayout(this);
            newPost.setOrientation(LinearLayout.HORIZONTAL);
            newPost.setBackground(getDrawable(R.drawable.rounded_984f4f));
            newPost.setLayoutParams(postParams);
            TextView newPostBest = new TextView(this);
            newPostBest.setPadding(48, 0, 0, 0);
            newPostBest.setTextColor(0xFFFFEE00);
            if (mybmarkbest[i]) {
                newPostBest.setText("★");
            }
            TextView newPostTitle = new TextView(this);
            newPostTitle.setPadding(24, 18, 0, 18);
            newPostTitle.setText(mybmarktitles[i]);
            newPostTitle.setTextColor(Color.WHITE);
            if (newPostTitle.getText().length() >= 16) {
                newPostTitle.setText(newPostTitle.getText().subSequence(0, 16) + "...");
            }
            TextView newPostCom = new TextView(this);
            newPostCom.setPadding(0, 0, 48, 0);
            newPostCom.setText("[" + mybmarkcom[i] + "]");
            newPostCom.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            newPostCom.setTextColor(0xFF91CAB1);
            newPostCom.setTypeface(newPostCom.getTypeface(), Typeface.BOLD);
            newPostCom.setLayoutParams(comParams);
            newPost.addView(newPostBest);
            newPost.addView(newPostTitle);
            newPost.addView(newPostCom);
            myBmarks.addView(newPost);
        }
    }


}
