package com.example.medicalai.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medicalai.R;

import static com.example.medicalai.MainActivity.HOST;
import static com.example.medicalai.MainActivity.cameraButton2;

public class HomeFragment extends Fragment {

    public static int out_fragm = 0; // 0 - Root, 1 - result
    public static int fragm = 0;

    public static View result;

    public static View manual, profile, disease, root;

    public static ViewGroup cont;

    private String SERVER = HOST;
    private final int CAMERA_CODE = 10, GALLERY_CODE = 11;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        cont = container;
        // Initializing the fragments

        root = inflater.inflate(R.layout.fragment_home,container,false);
        result = inflater.inflate(R.layout.fragment_output,null,false);
        disease = inflater.inflate(R.layout.fragment_disease,container,false);
        manual = inflater.inflate(R.layout.fragment_manual,null,false);
        profile = inflater.inflate(R.layout.fragment_settings,null,false);



        cameraButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return root;

    }


}



