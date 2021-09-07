package com.example.medicalai.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medicalai.LoginActivity;
import com.example.medicalai.MainActivity;
import com.example.medicalai.R;

import static com.example.medicalai.MainActivity.age;
import static com.example.medicalai.MainActivity.email;
import static com.example.medicalai.MainActivity.fname;
import static com.example.medicalai.MainActivity.lname;
import static com.example.medicalai.ui.home.HomeFragment.profile;

public class ProfileFragment extends Fragment {
    private Button logoutButton;
    public TextView em,gender,dob,fullname,welcome;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        em = profile.findViewById(R.id.emailTextView);
        welcome = profile.findViewById(R.id.welcomeText);
        gender = profile.findViewById(R.id.genderTextView);
        dob = profile.findViewById(R.id.dobTextView);
        fullname = profile.findViewById(R.id.fullnameTextView);

        welcome.setText("Welcome, "+fname+"!");

        em.setText(email);
        if(MainActivity.gender.equals("0")){
            gender.setText("Male");
        }else{
            gender.setText("Female");
        }
        dob.setText(age);
        fullname.setText(fname+" "+lname);


        logoutButton = profile.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Logout","yes");
                getActivity().getApplicationContext().getSharedPreferences("loginInfo",Context.MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return profile;
    }

    private void changeHost(String ip){
        ((MainActivity)this.getActivity()).setHOST(ip);
    }
}