package com.example.medicalai;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Random;

import static com.example.medicalai.HelperFunctions.hashMapToUrl;
import static com.example.medicalai.MainActivity.email;
import static com.example.medicalai.ui.disease.DiseaseFragment.accuracy;
import static com.example.medicalai.ui.disease.DiseaseFragment.imgReturned;
import static com.example.medicalai.ui.disease.DiseaseFragment.returnString;
import static com.example.medicalai.ui.home.HomeFragment.cont;
import static com.example.medicalai.ui.home.HomeFragment.disease;
import static com.example.medicalai.ui.home.HomeFragment.out_fragm;
import static com.example.medicalai.ui.home.HomeFragment.result;


public class UploadImage extends AsyncTask<Void,Void,String> {
    private Context context;
    private Bitmap image;
    private String id;
    private TextView view;
    public String SERVER;
    ViewGroup container;
    public UploadImage(Bitmap image, String host, TextView view, Context context, final ViewGroup container){
        this.context = context;
        this.image = image;
        this.SERVER = host;
        this.view = view;

        this.container = container;

        this.view.setText("Processing picture. Please wait");

    }

    @Override
    protected String doInBackground(Void... params) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        HashMap<String,String> detail = new HashMap<>();
        detail.put("email",email);
        detail.put("image", imageEncoded);

        try{
            String dataToSend = hashMapToUrl(detail);

            // We receive the % and the image back. The server
            // Separates them by space so we need to use the split function
            // to get them.

            String response = Request.post(SERVER,dataToSend);
            String detected = response.split(" ")[0];

            String acc = response.split(" ")[1];
            String imageReceived = response.split(" ")[2];

            // Add the image in the imageView
            byte[] bImg = Base64.decode(imageReceived,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bImg , 0, bImg.length);
            output(detected,acc,bitmap);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return "";
    }

    private void output(String detected,String acc,Bitmap bitmap){
        // Setting the image with the one returned from the server
        imgReturned.setImageBitmap(bitmap);

        // Writing the accuracy in the format 99.9%. Right now is 0.999
        float a = Float.parseFloat(acc)*100;
        String returnAcc = String.valueOf(a)+"%";
        int detection = Integer.parseInt(detected);
        // We have some cases to know how to color the text
        // and what output to write
        String out = "";

        // We have two cases, if what has been detected is more likely to be safe or not
        if(detection == 0){
            // Safe

            if(a<60) {
                accuracy.setTextColor(Color.YELLOW);
                out = "The server returned back a confidence of "+returnAcc+" that the "+
                        "image contains no lesion. This means that the highlighted area might be benign, but " +
                        "it is not really sure.\n\n"+
                        "If the image does not highlight any area that means that it is likely the server "+
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
            }else{
                accuracy.setTextColor(Color.GREEN);
                out = "The server returned back a confidence of "+returnAcc+" that the "+
                        "image contains a lesions. You should not worry about it, as it most likely " +
                        "means that the highlighted area is benign\n\n"+
                        "If the image does not highlight any area that means that it is likely the server "+
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
            }

        }if(detection == 1){
            // Not safe

            if(a<60) {
                accuracy.setTextColor(Color.YELLOW);
                out = "The server returned back a confidence of "+returnAcc+" that the "+
                        "image contains a lesion. This means that the highlighted area might be malignant, but " +
                        "it is not really sure.\n\n"+
                        "If the image does not highlight any area that means that it is likely the server "+
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
            }
            // Red - most likely there is something wrong
            accuracy.setTextColor(Color.RED);
            out = "The server returned back a confidence of "+returnAcc+" that the "+
                    "image contains a lesions. A confidence this high usually "+
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
            cont.removeView(disease);
            cont.addView(result);
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
