package com.example.medicalai;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.medicalai.HelperFunctions.encrypt_pass;

public class RegisterActivity extends AppCompatActivity {
    TextView email,password,ageButton;
    Button registerButton;
    ImageButton back;
    RadioButton male,female;
    RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (TextView) findViewById(R.id.emailText);
        password = (TextView) findViewById(R.id.passwordText);
        ageButton = (TextView) findViewById(R.id.ageText);
        rg = (RadioGroup) findViewById(R.id.radio);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener(){
            String em, passw;
            String gender;
            int age;
            String radio;

            @Override
            public void onClick(View view) {
                em = email.getText().toString();
                passw = password.getText().toString();
                passw = encrypt_pass(passw);
                age = Integer.parseInt(ageButton.getText().toString());


                Log.d("Info",em);
                Log.d("Info",passw);
                Log.d("Info",ageButton.getText().toString());
                Log.d("Info",em);


            }
        });
    }

}
