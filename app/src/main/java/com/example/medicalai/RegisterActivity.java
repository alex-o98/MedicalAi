package com.example.medicalai;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.example.medicalai.HelperFunctions.encrypt_pass;
import static com.example.medicalai.HelperFunctions.hashMapToUrl;
import static com.example.medicalai.MainActivity.HOST;

public class RegisterActivity extends AppCompatActivity {
    TextView email,password,fname,lname;
    Button registerButton,ageButton;
    ImageButton backButton;
    RadioButton male,female;
    RadioGroup rg;
    CheckBox tnc;
    DatePickerDialog dateDialog;
    Calendar cal = Calendar.getInstance();
    public String SERVER = HOST+"/register";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = findViewById(R.id.fnameText);
        lname = findViewById(R.id.lnameText);
        email = (TextView) findViewById(R.id.emailText);
        password = (TextView) findViewById(R.id.passwordText);

        rg = (RadioGroup) findViewById(R.id.radio);
        registerButton = (Button) findViewById(R.id.createButton);
        ageButton = findViewById(R.id.ageButton);
        backButton = findViewById(R.id.backButton);
        male = findViewById(R.id.maleButton);
        female = findViewById(R.id.femaleButton);

        tnc = findViewById(R.id.checkBox);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setAge();
            }

        };

        ageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){
            String em = "", passw = "";
            int gender = -1;
            String age = "";

            @Override
            public void onClick(View view) {
                try {
                    em = email.getText().toString();
                    String fn = fname.getText().toString();
                    String ln = lname.getText().toString();
                    if(fn.equals("")){
                        throw new Exception("All fields need to be entered");
                    }
                    if(ln.equals("")){
                        throw new Exception("All fields need to be entered");
                    }
                    if(em.equals("")){
                        throw new Exception("All fields need to be entered");
                    }else if(!em.contains("@") || !em.contains(".")){
                        throw new Exception("Email doesn't have the correct format");
                    }
                    passw = password.getText().toString();
                    if(passw.equals("")){
                        throw new Exception("All fields need to be entered");
                    }else if(passw.length()<6){
                        throw new Exception("Password needs to have more than 6 characters");
                    }
                    passw = encrypt_pass(passw);
                    age = ageButton.getText().toString();

                    if(age.equals("Age")){
                        throw new Exception("All fields need to be entered");
                    }

                    if (male.isChecked()) {
                        gender = 0;
                    } else if (female.isChecked()) {
                        gender = 1;
                    }else{
                        throw new Exception("All fields need to be entered");
                    }
                    if (!tnc.isChecked()){
                        throw new Exception("You need to agree to the terms and conditions before creating an account");
                    }
                    Log.d("InfoPass", passw);
                    Log.d("Info", em);
                    Log.d("Info", age);
                    Log.d("Info", String.valueOf(gender));


                    // Values to send: em passw age gender
                    HashMap<String,String> detail = new HashMap<>();
                    detail.put("email", em);
                    detail.put("password", passw);
                    detail.put("age",age);
                    detail.put("gender",String.valueOf(gender));
                    detail.put("fname",fname.getText().toString());
                    detail.put("lname",lname.getText().toString());

                    try {
                        String dataToSend = hashMapToUrl(detail);
                        AsyncRequest register = new AsyncRequest(dataToSend,SERVER);
                        String response = register.execute().get();
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
                            edit.putString("fname",fn);
                            edit.putString("lname",ln);

                            edit.apply();

                            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);

                            startActivity(myIntent);
                            finish();
                        }else{
                            dispalyDialog(response.split("%")[1]);
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }catch(Exception e){
                    Log.d("Exception",e.toString());
                }

            }
        });
    }

    private void setAge() {
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        ageButton.setText(sdf.format(cal.getTime()));
    }

    public static int getAge(int d, int m, int y) {
        return (int) ChronoUnit.YEARS.between(
                LocalDate.of( y , m , d) ,
                LocalDate.now( ZoneId.of("GMT") )
        );
    }
    public void dispalyDialog(String m){
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("Error")
                .setMessage(m)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
