package com.example.medicalai;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncRequest extends AsyncTask<Void,Void,String> {

    private String data;
    private String SERVER;

    public AsyncRequest(String data, String SERVER){
        this.data = data;
        this.SERVER = SERVER;
    }


    @Override
    protected String doInBackground(Void... voids) {

        try {
            String response = Request.post(SERVER,this.data);

            Log.d("RESPONE:",""+response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
