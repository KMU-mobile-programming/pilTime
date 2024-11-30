package com.example.piltime.Activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.R;

public class ProfileSettingsActivity extends AppCompatActivity {

    boolean ispersonalprivate = true;
    boolean amiboy = true;

    int profilepic = R.drawable.ic_launcher_foreground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        ImageView profileSetPic = (ImageView) findViewById(R.id.profileSetPic);
        profileSetPic.setImageDrawable(getDrawable(profilepic));

        LinearLayout profileSetMaleB = (LinearLayout) findViewById(R.id.profileSetMaleB);
        LinearLayout profileSetFemaleB = (LinearLayout) findViewById(R.id.profileSetFemaleB);
        TextView profileSetMale = (TextView) findViewById(R.id.profileSetMale);
        TextView profileSetFemale = (TextView) findViewById(R.id.profileSetFemale);
        if (amiboy) {
            profileSetMaleB.setBackground(getDrawable(R.drawable.rounded_984f4f));
            profileSetFemaleB.setBackground(null);
            profileSetMale.setTextColor(0xFFFFFFFF);
            profileSetFemale.setTextColor(0xFF000000);
        }
        else {
            profileSetMaleB.setBackground(null);
            profileSetFemaleB.setBackground(getDrawable(R.drawable.rounded_984f4f));
            profileSetMale.setTextColor(0xFF000000);
            profileSetFemale.setTextColor(0xFFFFFFFF);
        }

        LinearLayout personalB = (LinearLayout) findViewById(R.id.personalB);
        ImageView personalPrivateS = (ImageView) findViewById(R.id.personalPrivateS);
        TextView personalOnOff = (TextView) findViewById(R.id.personalOnOff);
        if (ispersonalprivate) {
            personalB.setGravity(Gravity.LEFT);
            personalPrivateS.setImageDrawable(getDrawable(R.drawable.rounded_984f4f));
            personalOnOff.setText("OFF");
            personalOnOff.setTextColor(0xFF984F4F);
        }
        else {
            personalB.setGravity(Gravity.RIGHT);
            personalPrivateS.setImageDrawable(getDrawable(R.drawable.rounded_f3d1a5));
            personalOnOff.setText("ON");
            personalOnOff.setTextColor(0xFFF07070);
        }

        LinearLayout personalPrivate = (LinearLayout) findViewById(R.id.personalPrivate);
        personalPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ispersonalprivate = !ispersonalprivate;
                if (ispersonalprivate) {
                    personalB.setGravity(Gravity.LEFT);
                    personalPrivateS.setImageDrawable(getDrawable(R.drawable.rounded_984f4f));
                    personalOnOff.setText("OFF");
                    personalOnOff.setTextColor(0xFF984F4F);
                }
                else {
                    personalB.setGravity(Gravity.RIGHT);
                    personalPrivateS.setImageDrawable(getDrawable(R.drawable.rounded_f3d1a5));
                    personalOnOff.setText("ON");
                    personalOnOff.setTextColor(0xFFF07070);
                }
            }
        });

        LinearLayout profileSetSex = (LinearLayout) findViewById(R.id.profileSetSex);
        profileSetSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amiboy = !amiboy;
                if (amiboy) {
                    profileSetMaleB.setBackground(getDrawable(R.drawable.rounded_984f4f));
                    profileSetFemaleB.setBackground(null);
                    profileSetMale.setTextColor(0xFFFFFFFF);
                    profileSetFemale.setTextColor(0xFF000000);
                }
                else {
                    profileSetMaleB.setBackground(null);
                    profileSetFemaleB.setBackground(getDrawable(R.drawable.rounded_984f4f));
                    profileSetMale.setTextColor(0xFF000000);
                    profileSetFemale.setTextColor(0xFFFFFFFF);
                }
            }
        });
    }
}
