package com.example.medicalai;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Random;

import static com.example.medicalai.HelperFunctions.hashMapToUrl;
import static com.example.medicalai.ui.disease.DiseaseFragment.accuracy;
import static com.example.medicalai.ui.disease.DiseaseFragment.out_fragm;
import static com.example.medicalai.ui.disease.DiseaseFragment.imgReturned;
import static com.example.medicalai.ui.disease.DiseaseFragment.returnString;


public class UploadImage extends AsyncTask<Void,Void,String> {
    private Context context;
    private Bitmap image;
    private String id;
    private TextView view;
    public String SERVER;
    ViewGroup container;
    View root;
    View result;
    public UploadImage(Bitmap image, String host, TextView view, Context context, final ViewGroup container, final View root, final View result){
        this.context = context;
        this.image = image;
        this.id = randomID();
        this.SERVER = host;
        this.view = view;

        this.container = container;
        this.root = root;
        this.result = result;

        this.view.setText("Processing picture. Please wait");

    }

    @Override
    protected String doInBackground(Void... params) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        HashMap<String,String> detail = new HashMap<>();
        detail.put("id", id);
        detail.put("image", imageEncoded);

        try{
            String dataToSend = hashMapToUrl(detail);

            // We receive the % and the image back. The server
            // Separates them by space so we need to use the split function
            // to get them.

            String response = Request.post(SERVER,dataToSend);

            String acc = response.split(" ")[0];
            String imageReceived = response.split(" ")[1];

            // Add the image in the imageView
            byte[] bImg = Base64.decode(imageReceived,Base64.DEFAULT);

            Bitmap bitmap = BitmapFactory.decodeByteArray(bImg , 0, bImg .length);

            output(acc,bitmap);


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return "";
    }

    private void output(String acc,Bitmap bitmap){
        // Setting the image with the one returned from the server
        imgReturned.setImageBitmap(bitmap);

        // Writing the accuracy in the format 99.9%. Right now is 0.999
        float a = Float.parseFloat(acc)*100;
        String returnAcc = String.valueOf(a)+"%";

        // We have some cases to know how to color the text
        // and what output to write
        String out = "";
        if(a < 20){
            // Green - safe
            accuracy.setTextColor(Color.GREEN);
            out = "The server returned back a confidence of "+returnAcc+" that the "+
                    "image contains a lesions. A confidence of less than 20% usually "+
                    "means that the highlighted area is benign\n\n"+
                    "If the image does not highlight any area that means that it is likely the server "+
                    "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                    "in a different way.\n\n" +
                    "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                    "as a guidance rather than a diagnostic";
        }else if(a>=20 && a<65){
            // Yellow - be aware
            accuracy.setTextColor(Color.YELLOW);
            out = "The server returned back a confidence of "+returnAcc+" that the "+
                    "image contains a lesions. A confidence higher than 20% and less than 65% usually "+
                    "means that the highlighted area might be malignant\n\n"+
                    "If the image does not highlight any area that means that it is likely the server "+
                    "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                    "in a different way.\n\n" +
                    "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                    "as a guidance rather than a diagnostic";
        }else if(a>=65){
            // Red - most likely there is something wrong
            accuracy.setTextColor(Color.RED);
            out = "The server returned back a confidence of "+returnAcc+" that the "+
                    "image contains a lesions. A confidence higher than 65% usually "+
                    "means that the highlighted area is very likely to be malignant\n\n"+
                    "If the image does not highlight any area that means that it is likely the server "+
                    "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                    "in a different way.\n\n" +
                    "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                    "as a guidance rather than a diagnostic";
        }
        accuracy.setText(returnAcc);
        returnString.setText(out);

    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null){
            // If everything worked out, we change to the output fragment
            this.container.removeView(this.root);
            this.container.addView(this.result);
            out_fragm = 1;
        }else{
            new AlertDialog.Builder(this.context)
                    .setTitle("Server error")
                    .setMessage("There was an error connecting to the server. Try again later")
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            this.view.setText("");
        }

    }

    private String randomID(){
        String ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(12);
        for( int i = 0; i < 12; i++ )
            sb.append( ALLOWED_CHARS.charAt( rnd.nextInt(ALLOWED_CHARS.length()) ) );
        return sb.toString();
    }
}
