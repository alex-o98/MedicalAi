package com.example.medicalai.ui.disease;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.medicalai.R;
import com.example.medicalai.UploadImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.medicalai.MainActivity.HOST;
import static com.example.medicalai.MainActivity.cameraButton2;
import static com.example.medicalai.MainActivity.resetOutput;
import static com.example.medicalai.ui.home.HomeFragment.cont;
import static com.example.medicalai.ui.home.HomeFragment.disease;
import static com.example.medicalai.ui.home.HomeFragment.fragm;
import static com.example.medicalai.ui.home.HomeFragment.gallery;
import static com.example.medicalai.ui.home.HomeFragment.last;
import static com.example.medicalai.ui.home.HomeFragment.lastFrag;
import static com.example.medicalai.ui.home.HomeFragment.manual;
import static com.example.medicalai.ui.home.HomeFragment.out_fragm;
import static com.example.medicalai.ui.home.HomeFragment.profile;
import static com.example.medicalai.ui.home.HomeFragment.result;
import static com.example.medicalai.ui.home.HomeFragment.root;
public class DiseaseFragment extends Fragment {


    public static Button cameraButton,uploadButton,sendButton,cancelButton;
    public static ImageView imgTaken;
    public static Uri photoURI;
    public static File photoFile = null;
    public static TextView resultText;

    public static ImageView imgReturned;
    public static TextView accuracy;
    public static TextView returnString, resultView;


    public static Bitmap bitmap;


    private String SERVER = HOST;
    private final int CAMERA_CODE = 10, GALLERY_CODE = 11;



    public void selection(int choice) {

        hideButtons();

        if(choice == 0) {
            // Camera
            takePicture();

        }
        else if (choice == 1) {
            // Upload
            selectPicture();
        }

    }

    public int[] cameraOrGallery(Context self){
        final int[] res = {-1};
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(self);
        dlgAlert.setTitle("Take a picture or upload one?");
        dlgAlert.setMessage("Please select if you want to take a picture with your camera, or if you want to upload an existing one from the gallery");

        dlgAlert.setPositiveButton("Take picture",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                selection(0);
            }
        });
        dlgAlert.setNegativeButton("Upload picture", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection(1);
            }
        });
        dlgAlert.show();
        return res;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendButton = disease.findViewById(R.id.sendButton);
        cancelButton = disease.findViewById(R.id.cancelButton);

        resultText = disease.findViewById(R.id.resultText);

    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {


        final ViewGroup container2 = container;
        final View r = inflater.inflate(R.layout.fragment_home,null,false);


        sendButton = disease.findViewById(R.id.sendButton);
        cancelButton = disease.findViewById(R.id.cancelButton);

        resultText = disease.findViewById(R.id.resultText);
        imgTaken = disease.findViewById(R.id.imageTaken);

        // Now it comes the output part initialization and processing
        imgReturned = result.findViewById(R.id.returnedImage);

        accuracy = result.findViewById(R.id.accuracyTextView);
        returnString = result.findViewById(R.id.resultStringTextView);
        resultView = result.findViewById(R.id.resultView);


        cameraButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer x,y;
                x=fragm;
                y=out_fragm;
                Log.d("Data:","Fragm "+x.toString());
                Log.d("Data:","Out_Fragm "+y.toString());
                if (fragm == 0){
                    last = root;
                    lastFrag = 0;

                }else if(fragm == 1){
                    last = manual;
                    lastFrag = 1;
                }else if(fragm == 3){
                    last = profile;
                    lastFrag = 3;
                }else if(fragm == 2){
                    last = gallery;
                    lastFrag = 2;
                }
                if(out_fragm == 1){
                    resetOutput();
                }
                cameraOrGallery(getContext());

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOutput();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    bitmap = ((BitmapDrawable) imgTaken.getDrawable()).getBitmap();

                    if(checkImage(bitmap)) {
                        // We also send both views, in order to activate them accordingly.
                        UploadImage upload = new UploadImage(bitmap,HOST + "/sendImage",resultText, getActivity(), container);
                        upload.execute();
                    }else{
                        Toast.makeText(getActivity(),"Image incorrect. Go to the user manual from the left panel for more information.",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return r;

    }

    public void addDiseaseView(){
        cont.addView(disease);
        cont.removeView(last);
    }

    private void showButtons(){
        sendButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);

    }
    private void hideButtons(){
        sendButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
    }
    private boolean checkImage(Bitmap bmp){
        if(bmp.getWidth() == bmp.getHeight()){
            if(bmp.getWidth()>128 && bmp.getHeight() >128){
                return true;
            }
        }

        return false;


    }
    public void resetPicture() {
        // Reset the image view
        imgTaken.setImageResource(android.R.color.transparent);

        // Reset the buttons
        sendButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);

        // Reset the text
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
    public void selectPicture(){
        // We rotate the ImageView before selecting it
        imgTaken.setRotation(0);
        imgReturned.setRotation(0);
        Intent selectPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectPictureIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(selectPictureIntent,"Select a picture"),GALLERY_CODE);
    }
    public void takePicture() {
        // We rotate the ImageView before taking a picture
        imgTaken.setRotation(90);
        imgReturned.setRotation(90);
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
            showButtons();
            addDiseaseView();
        }else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            photoURI = data.getData();
            imgTaken.setImageURI(photoURI);
            showButtons();
            addDiseaseView();
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



