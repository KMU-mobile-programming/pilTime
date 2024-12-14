package com.example.piltime.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.R;

public class ProfileActivity extends AppCompatActivity {

    String mynickname = "피곤한감자";
    int myage = 21;
    boolean amiboy = true;

    int profilepic = R.drawable.ic_launcher_foreground;

    String[] mypills = new String[] {
            "암페타민",
            "프로포폴",
            "모르핀",
            "코카인"
    };
    int[] mypillimgs = new int[] {
            R.drawable.rounded_984f4f,
            R.drawable.rounded_f3d1a5,
            R.drawable.ic_launcher_background,
            R.drawable.background_rounded_corners
    };

    String[] myposttitles = new String[] {
            "우하하하",
            "간장공장공자앚앚아장장장장자앚앚아",
            "무슨 마약하시길래 이런 생각을 했어요?",
            "우히히히",
            "즐" };
    boolean[] mypostbest = new boolean[] {
            true, true, false, true, false
    };
    int[] mypostcom = new int[] {
            1, 69, 78, 34, 2
    };

    String[] mybmarktitles = new String[] {
            "크핫핫핫",
            "간장공장공장장은 강공장장장장장",
            "무슨 마약하시길래 이런 생각을 했어요?",
            "우히히히",
            "즐" };
    boolean[] mybmarkbest = new boolean[] {
            false, false, false, true, false
    };
    int[] mybmarkcom = new int[] {
            5, 6, 78, 9, 1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
        profilePic.setImageDrawable(getDrawable(profilepic));

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
        }
        else {
            profilePersonal.setText(myage + " / 여");
        }

        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(240,240);
        imgParams.setMargins(20,20,20,20);
        LinearLayout.LayoutParams postParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        postParams.setMargins(0,12,0,12);
        LinearLayout.LayoutParams comParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout myPills = (LinearLayout) findViewById(R.id.myPills);
        for (int i = 0; i < mypills.length; i++) {
            LinearLayout newPill = new LinearLayout(this);
            newPill.setOrientation(LinearLayout.VERTICAL);
            ImageView newPillImg = new ImageView(this);
            newPillImg.setImageDrawable(getDrawable(mypillimgs[i]));
            newPillImg.setBackground(getDrawable(R.drawable.background_rounded_corners));
            newPillImg.setClipToOutline(true);
            newPillImg.setLayoutParams(imgParams);
            TextView newPillTxt = new TextView(this);
            newPillTxt.setText(mypills[i]);
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
            newPostBest.setPadding(48,0,0,0);
            newPostBest.setTextColor(0xFFFFEE00);
            if (mypostbest[i]) {
                newPostBest.setText("★");
            }
            TextView newPostTitle = new TextView(this);
            newPostTitle.setPadding(24,18,0,18);
            newPostTitle.setText(myposttitles[i]);
            newPostTitle.setTextColor(Color.WHITE);
            if (newPostTitle.getText().length() >= 16) {
                newPostTitle.setText(newPostTitle.getText().subSequence(0,16)+"...");
            }
            TextView newPostCom = new TextView(this);
            newPostCom.setPadding(0,0,48,0);
            newPostCom.setText("["+mypostcom[i]+"]");
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
            newPostBest.setPadding(48,0,0,0);
            newPostBest.setTextColor(0xFFFFEE00);
            if (mybmarkbest[i]) {
                newPostBest.setText("★");
            }
            TextView newPostTitle = new TextView(this);
            newPostTitle.setPadding(24,18,0,18);
            newPostTitle.setText(mybmarktitles[i]);
            newPostTitle.setTextColor(Color.WHITE);
            if (newPostTitle.getText().length() >= 16) {
                newPostTitle.setText(newPostTitle.getText().subSequence(0,16)+"...");
            }
            TextView newPostCom = new TextView(this);
            newPostCom.setPadding(0,0,48,0);
            newPostCom.setText("["+mybmarkcom[i]+"]");
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
