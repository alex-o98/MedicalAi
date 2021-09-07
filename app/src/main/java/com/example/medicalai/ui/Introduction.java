package com.example.medicalai.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.medicalai.LoginActivity;
import com.example.medicalai.MainActivity;
import com.example.medicalai.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class Introduction extends AppIntro {
        SharedPreferences seen;
        SharedPreferences login;

        @Override
        protected void onCreate(Bundle saveInstanceState){
            super.onCreate(saveInstanceState);
            isColorTransitionsEnabled();
            seen = this.getSharedPreferences("seenIntro", MODE_PRIVATE);
            String s = seen.getString("seen","");

            // User has already seen this introduction. Now whe check to see if it's logged in
            if(s.equals("y")){
                login = this.getSharedPreferences("loginInfo", MODE_PRIVATE);
                String email = login.getString("email","");
                // If logged in, we skip to the MainActivity
                if(!email.equals("")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            // First slide
            setImmersiveMode();
            addSlide(AppIntroFragment.newInstance("Introduction","Welcome to MedicalAI! You can use this app to check for different skin diseases.",0,R.color.colorPrimaryLight));
            addSlide(AppIntroFragment.newInstance("Test for lesions","In order to test for a disease, you can take a picture on the spot, or upload one from gallery. The aspect ratio has to be 1:1!",0,R.color.colorAccent));
            addSlide(AppIntroFragment.newInstance("Keep track of your previous entries","If you go to the gallery, you can see your previous results",0,R.color.colorPrimaryDark));

        }

        @Override
        public void onDonePressed(Fragment frag){
            super.onDonePressed(frag);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            SharedPreferences.Editor edit = seen.edit();
            edit.putString("seen","y");
            edit.apply();

            startActivity(intent);
            finish();
        }

        @Override
        public void onSkipPressed(Fragment frag){
            super.onSkipPressed(frag);
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            SharedPreferences.Editor edit = seen.edit();
            edit.putString("seen","y");
            edit.apply();

            startActivity(intent);
            finish();
        }


}

