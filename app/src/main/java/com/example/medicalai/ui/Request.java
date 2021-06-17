package com.example.medicalai.ui;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {

    private static final String TAG = Request.class.getSimpleName();

    public static String post(String serverUrl,String dataToSend){
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1000 * 30);
            con.setReadTimeout(1000 * 30);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(dataToSend);
            writer.flush();
            writer.close();
            os.close();
            int responseCode = con.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){

                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null){
                    sb.append(line).append("\n");
                }

                return sb.toString();
            }else{
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}