package com.example.medicalai.ui.disease;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medicalai.R;

import java.io.File;

import static com.example.medicalai.MainActivity.HOST;
import static com.example.medicalai.MainActivity.cameraButton2;

public class DiseaseFragment extends Fragment {


    public static Button cameraButton,uploadButton,sendButton,cancelButton;
    public static ImageView imgTaken;
    private Uri photoURI;
    private File photoFile = null;
    public static TextView resultText;

    public static ImageView imgReturned;
    public static TextView accuracy;
    public static TextView returnString;


    private Bitmap bitmap;

    public static int out_fragm = 0; // 0 - Root, 1 - result
    public static int fragm = 0;
    public static View root;
    public static View result;

    public static View manual;
    public static View profile;

    public static ViewGroup cont;

    private String SERVER = HOST;
    private final int CAMERA_CODE = 10, GALLERY_CODE = 11;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        cont = container;
        // Initializing the fragments

        root = inflater.inflate(R.layout.fragment_disease,container,false);
        result = inflater.inflate(R.layout.fragment_output,null,false);

        manual = inflater.inflate(R.layout.fragment_manual,null,false);
        profile = inflater.inflate(R.layout.fragment_settings,null,false);

        cameraButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TST","TST");
            }
        });

        return root;

    }


}



