package com.example.medicalai;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.medicalai.HelperFunctions.encrypt_pass;
import static com.example.medicalai.HelperFunctions.hashMapToUrl;
import static com.example.medicalai.MainActivity.HOST;


public class LoginActivity extends AppCompatActivity {
    TextView email,password;
    Button loginButton,registerButton;
    public String SERVER = HOST+"/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (TextView) findViewById(R.id.emailText);
        password = (TextView) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.regButton);

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


                try {
                    String dataToSend = hashMapToUrl(detail);
                    AsyncRequest login = new AsyncRequest(dataToSend,SERVER);

                    String response = login.execute().get();
                    Log.d("response",response);
                    String action = response.split("%")[0];

                    if(Integer.parseInt(action) == 0) {
                        String email = response.split("%")[1];
                        String dob = response.split("%")[2];
                        String gender = response.split("%")[3];
                        String fname = response.split("%")[4];
                        String lname = response.split("%")[5];


                        int d = Integer.parseInt(dob.split("\\.")[0]);
                        int m = Integer.parseInt(dob.split("\\.")[1]);
                        int y = Integer.parseInt(dob.split("\\.")[2]);

                        int age = getAge(d,m,y);
                        Integer x = age;



                        SharedPreferences loginPrefference = getApplicationContext().getSharedPreferences("loginInfo",MODE_PRIVATE);
                        SharedPreferences.Editor edit = loginPrefference.edit();
                        edit.putString("email",email);
                        edit.putString("gender",gender);
                        edit.putString("age",x.toString());
                        edit.putString("fname",fname);
                        edit.putString("lname",lname);

                        edit.apply();

                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
                        finish();
                    }else{
                        dispalyDialog(response.split("%")[1]);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public static int getAge(int d, int m, int y) {
        return (int)ChronoUnit.YEARS.between(
                LocalDate.of( y , m , d) ,
                LocalDate.now( ZoneId.of("GMT") )
        );
    }
    public void dispalyDialog(String m){
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Error")
                .setMessage(m)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
