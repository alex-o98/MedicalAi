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
import static com.example.medicalai.ui.disease.DiseaseFragment.resultView;
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
            String detected = response.split("%")[0];

            String acc = response.split("%")[1];
            String imageReceived = response.split("%")[2];

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

        // We have some cases to know how to color the text
        String out = "";

        // Benign
        switch (detected) {
            case "Keratosis-like lesion":
                accuracy.setTextColor(Color.GREEN);
                out += "The server returned back a confidence of " + returnAcc + " that the " +
                        "image contains a Keratosis-like lesion. This lesion is a noncancerous one (benign) " +
                        "and there is nothing to worry about it, as they can not become harmful, and in fact " +
                        "most of the people develop one in their lifetime. Because of this, it does not need to be removed\n\n" +
                        "If the image does not highlight any area that means that it is likely the server " +
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
                break;
            case "Melanocytic nevi":
                accuracy.setTextColor(Color.GREEN);
                out += "The server returned back a confidence of " + returnAcc + " that the " +
                        "image contains a Melanocytic nevi, also called as a mole. This lesion is a noncancerous one (benign) " +
                        "and is due to a local proliferation of pigment cells. However, about 25% of melanomas develop from moles, " +
                        "so if you see something strange about it or you have seen it develop over time, you should seek a doctor, " +
                        "otherwise it does not need to be removed\n\n" +
                        "If the image does not highlight any area that means that it is likely the server " +
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
                break;
            case "Dermatofibroma":
                accuracy.setTextColor(Color.GREEN);
                out += "The server returned back a confidence of " + returnAcc + " that the " +
                        "image contains a Dermatofibroma. This lesion is a noncancerous one (benign) " +
                        "and it has no chances of creating complications, due to the fact that it was most likely " +
                        "created after a minor trauma of the skin from insect bites, injections, or other stuff similar. " +
                        "Because of this, it is not needed to be removed or treated\n\n" +
                        "If the image does not highlight any area that means that it is likely the server " +
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
                break;
            case "Melanoma":
                accuracy.setTextColor(Color.RED);
                out += "The server returned back a confidence of " + returnAcc + " that the " +
                        "image contains a Melanoma. This lesion is a cancerous one (malignant) " +
                        "and it is recommended to go to the doctor as fast as possible. Depending on it's stage " +
                        "and how early it is detected, it can be harmless\n\n" +
                        "If the image does not highlight any area that means that it is likely the server " +
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
                break;
            case "Vascular lesion":
                accuracy.setTextColor(Color.GREEN);
                out += "The server returned back a confidence of " + returnAcc + " that the " +
                        "image contains a Vascular lesion. This lesion is a noncancerous one (benign) " +
                        "and it has no chances of creating complications.\n\n" +
                        "If the image does not highlight any area that means that it is likely the server " +
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
                break;
            case "Basal cell carcinoma":
                accuracy.setTextColor(Color.RED);
                out += "The server returned back a confidence of " + returnAcc + " that the " +
                        "image contains a Basal cell carcinoma. This lesion is a noncancerous one (benign) " +
                        "and because it grows slowly, it is easy to treat. However, patients with multiple Basal cell carcinoma" +
                        "diagnostics are more likely to develop other malignancies, so you should seek a doctor if this is not your first one." +
                        "The lesion needs to be treated so you should go to the doctor\n\n" +
                        "If the image does not highlight any area that means that it is likely the server " +
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
                break;
            case "Actinic keratosis":
                accuracy.setTextColor(Color.YELLOW);
                out += "The server returned back a confidence of " + returnAcc + " that the " +
                        "image contains a Actinic keratosis. This lesion is often categorized as precancer. " +
                        "It is not life-threatening, and if treated early they do not have the chance to develop into skin cancer." +
                        "The lesion needs to be treated so you should go to the doctor\n\n" +
                        "If the image does not highlight any area that means that it is likely the server " +
                        "did not see the spot on the skin correctly. If so, try again by taking another picture " +
                        "in a different way.\n\n" +
                        "Please note that this result does not take place of a specialist's opinion, and should be used only " +
                        "as a guidance rather than a diagnostic";
                break;
        }

        accuracy.setText(returnAcc);
        resultView.setText(detected);
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
