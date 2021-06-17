package com.example.medicalai.ui.disease;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.example.medicalai.ui.Request;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class UploadImage extends AsyncTask<Void,Void,String> {
    private Bitmap image;
    private String id;
    private TextView view;
    public String SERVER;
    public UploadImage(Bitmap image, String host, TextView view){
        this.image = image;
        this.id = randomID();
        this.SERVER = host;
        this.view = view;
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
            String response = Request.post(SERVER,dataToSend);
            Log.d("RESPONSE:",""+response);

            return response;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    @Override
    protected void onPostExecute(String s) {
        if(s!=null){
            this.view.setText(s);
        }else{
            Log.d("IP_FAIL:",SERVER);
            this.view.setText("There was an error processing your image. Please try again.");
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
