package com.example.medicalai.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.medicalai.LoginActivity;
import com.example.medicalai.MainActivity;
import com.example.medicalai.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class Introduction extends AppIntro {
        @Override
        protected void onCreate(Bundle saveInstanceState){
            super.onCreate(saveInstanceState);
            isColorTransitionsEnabled();
            // First slite
            setImmersiveMode();
            addSlide(AppIntroFragment.newInstance("Test","Test 1",R.drawable.ic_appintro_done,R.drawable.ic_launcher_background));
            addSlide(AppIntroFragment.newInstance("Test","Test 2",R.drawable.ic_appintro_done,R.color.colorAccent));
            addSlide(AppIntroFragment.newInstance("Test","Test 3",R.drawable.ic_appintro_done,R.color.colorPrimaryDark));

        }

        @Override
        public void onDonePressed(Fragment frag){
            super.onDonePressed(frag);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onSkipPressed(Fragment frag){
            super.onSkipPressed(frag);
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }


}

