package com.example.medicalai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static com.example.medicalai.HelperFunctions.encrypt_pass;
import static com.example.medicalai.HelperFunctions.hashMapToUrl;
import static com.example.medicalai.MainActivity.HOST;


public class LoginActivity extends AppCompatActivity {
    TextView email,password;
    Button loginButton,registerButton;
    public String SERVER = HOST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (TextView) findViewById(R.id.emailText);
        password = (TextView) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener(){
            String em, passw;
            @Override
            public void onClick(View v){
                em = email.getText().toString();
                passw = password.getText().toString();
                passw = encrypt_pass(passw);

                HashMap<String,String> detail = new HashMap<>();
                detail.put("email", em);
                detail.put("password", passw);

                SERVER = SERVER+"/login";

                try {
                    String dataToSend = hashMapToUrl(detail);
                    AsyncRequest login = new AsyncRequest(dataToSend,SERVER);
                    login.execute();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }




}
