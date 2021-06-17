package com.example.medicalai.ui.disease;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.medicalai.MainActivity;
import com.example.medicalai.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class DiseaseFragment extends Fragment {


    private Button cameraButton,uploadButton,sendButton,cancelButton;
    private ImageView imgTaken;
    private Uri photoURI;
    private File photoFile = null;

    private TextView resultText;

    private Bitmap bitmap;

    private String host;
    private final int CAMERA_CODE = 10, GALLERY_CODE = 11;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_disease, container, false);

        cameraButton = root.findViewById(R.id.cameraButton);
        uploadButton = root.findViewById(R.id.uploadButton);

        sendButton = root.findViewById(R.id.sendButton);
        cancelButton = root.findViewById(R.id.cancelButton);

        resultText = root.findViewById(R.id.resultTextView);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    bitmap = ((BitmapDrawable) imgTaken.getDrawable()).getBitmap();

                    if(checkImage(bitmap)) {
                        host = updateHost();
                        UploadImage upload = new UploadImage(bitmap,host,resultText);
                        upload.execute();

                    }else{
                        Toast.makeText(getActivity(),"Image incorrect. Go to the user manual from the left panel for more information.",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetPicture();
            }
        });

        imgTaken = root.findViewById(R.id.imageTaken);

        return root;

    }
    private String updateHost(){
        String tmp = ((MainActivity)this.getActivity()).getHOST() + "/sendImage";
        return tmp;
    }
    private void hideButtons(){
        sendButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        cameraButton.setVisibility(View.GONE);
        uploadButton.setVisibility(View.GONE);

    }
    private boolean checkImage(Bitmap bmp){
        if(bmp.getWidth() == bmp.getHeight()){
            if(bmp.getWidth()>128 && bmp.getHeight() >128){
                return true;
            }
        }

        return false;


    }
    private void resetPicture() {
        // Reset the image view
        imgTaken.setImageResource(android.R.color.transparent);

        // Reset the buttons
        sendButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        cameraButton.setVisibility(View.VISIBLE);
        uploadButton.setVisibility(View.VISIBLE);
        resultText.setText("");


    }
    private void updateViewFromUri(Uri uri){
        try{
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            imgTaken.setImageBitmap(bitmap);
        }
        catch (Exception e){

        }
    }
    private void selectPicture(){
        // We rotate the ImageView before selecting it
        imgTaken.setRotation(0);
        Intent selectPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectPictureIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(selectPictureIntent,"Select a picture"),GALLERY_CODE);
    }
    private void takePicture() {
        // We rotate the ImageView before taking a picture
        imgTaken.setRotation(90);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = null;
            photoFile = createImageFile();
            Log.d("TST:","Taking the pic");
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.medicalai.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            updateViewFromUri(photoURI);
            hideButtons();
        }else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            photoURI = data.getData();
            imgTaken.setImageURI(photoURI);
            hideButtons();
        }

    }

    private File createImageFile(){
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "Image_" + timeStamp + "_";
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
            photoFile = image;

            return image;
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }



}



