package com.example.medicalai.ui.disease;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.medicalai.ui.disease.DiseaseFragment.bitmap;
import static com.example.medicalai.ui.disease.DiseaseFragment.cameraButton;
import static com.example.medicalai.ui.disease.DiseaseFragment.cancelButton;
import static com.example.medicalai.ui.disease.DiseaseFragment.imgReturned;
import static com.example.medicalai.ui.disease.DiseaseFragment.imgTaken;
import static com.example.medicalai.ui.disease.DiseaseFragment.photoFile;
import static com.example.medicalai.ui.disease.DiseaseFragment.photoURI;
import static com.example.medicalai.ui.disease.DiseaseFragment.sendButton;
import static com.example.medicalai.ui.disease.DiseaseFragment.uploadButton;

public class DiseasePictures {

    Context context;
    Activity activity;

    public DiseasePictures(Context con, Activity acc){
        this.context = con;
        this.activity = acc;

    }

    public void takePicture() {
        // We rotate the ImageView before taking a picture
        imgTaken.setRotation(90);
        imgReturned.setRotation(90);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = null;
            photoFile = createImageFile(this.activity);
            Log.d("TST:","Taking the pic");
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this.context,
                        "com.example.medicalai.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, 10);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK) {
            updateViewFromUri(photoURI);
            hideButtons();
        }else if (requestCode == 11 && resultCode == RESULT_OK){
            photoURI = data.getData();
            imgTaken.setImageURI(photoURI);
            hideButtons();
        }

    }
    public void updateViewFromUri(Uri uri){
        try{
            bitmap = MediaStore.Images.Media.getBitmap(this.activity.getContentResolver(), uri);
            imgTaken.setImageBitmap(bitmap);
        }
        catch (Exception e){

        }
    }
    private void hideButtons(){
        sendButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        cameraButton.setVisibility(View.GONE);
        uploadButton.setVisibility(View.GONE);
    }

    private File createImageFile(Activity activity){
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "Image_" + timeStamp + "_";
            File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
            photoFile = image;

            return image;
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

}
