package com.example.medicalai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.medicalai.ui.disease.DiseaseFragment;
import com.example.medicalai.ui.disease.DiseasePictures;
import com.example.medicalai.ui.manual.ManualFragment;
import com.example.medicalai.ui.settings.ProfileFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.medicalai.HelperFunctions.hashMapToUrl;
import static com.example.medicalai.ui.disease.DiseaseFragment.cancelButton;
import static com.example.medicalai.ui.disease.DiseaseFragment.imgTaken;
import static com.example.medicalai.ui.disease.DiseaseFragment.resultText;
import static com.example.medicalai.ui.disease.DiseaseFragment.sendButton;
import static com.example.medicalai.ui.home.HomeFragment.cont;
import static com.example.medicalai.ui.home.HomeFragment.disease;
import static com.example.medicalai.ui.home.HomeFragment.fragm;
import static com.example.medicalai.ui.home.HomeFragment.gallery;
import static com.example.medicalai.ui.home.HomeFragment.last;
import static com.example.medicalai.ui.home.HomeFragment.lastFrag;
import static com.example.medicalai.ui.home.HomeFragment.manual;
import static com.example.medicalai.ui.home.HomeFragment.out_fragm;
import static com.example.medicalai.ui.home.HomeFragment.profile;
import static com.example.medicalai.ui.home.HomeFragment.result;
import static com.example.medicalai.ui.home.HomeFragment.root;

//import static com.example.medicalai.ui.disease.DiseaseFragment.

// Importing the contents from the HomeFragment so we can reset it

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private AppCompatActivity app;

    public static String email,gender,age,fname,lname;


    public static String HOST = "http://192.168.100.2:5000";
    public static FloatingActionButton cameraButton2;
    public static DiseasePictures pictureManager;
    public static FragmentManager fm;
    public static FragmentTransaction ft;
    public Fragment home,man,dis,prof;
    public void removeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        fragmentManager.popBackStack(fragment.getId(), 0);

    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawer_layout, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public void resetGallery(){
        imgTaken.setImageResource(android.R.color.transparent);

        // Reset the buttons
        sendButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);

        // Reset the text
        resultText.setText("");
    }
    public void getGallery(){

        HashMap<String,String> detail = new HashMap<>();
        detail.put("email", email);
        LinearLayout linearLayout = (LinearLayout) gallery.findViewById(R.id.layoutGallery);

        try {
            String dataToSend = hashMapToUrl(detail);
            AsyncRequest getData = new AsyncRequest(dataToSend,HOST+"/getRecords");

            String response = getData.execute().get();
            Log.d("Response:",response);
            String[] resp = response.split("&");
            Display display = getWindowManager().getDefaultDisplay();

            for(int i=0;i<resp.length;i++){
                String[] temp = resp[i].split("%");

                String imageReceived = temp[0];
                String date= temp[1];
                String result = temp[2];

                String accuracy = temp[3];
                float tmp = Float.parseFloat(accuracy);
                tmp = tmp*100;
                accuracy = Float.toString(tmp);


                byte[] bImg = Base64.decode(imageReceived,Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bImg , 0, bImg.length);

                LinearLayout.LayoutParams paramsLayoutHorizontal = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsLayoutHorizontal.setMargins(30,0,25,50);

                LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(20,0,0,0);

                LinearLayout toAddHorizontal = new LinearLayout(this);
                toAddHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                toAddHorizontal.setLayoutParams(paramsLayoutHorizontal);



                ImageView imgView = new ImageView(this);
                imgView.setLayoutParams(params);

                imgView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 250, 250, false));
                toAddHorizontal.addView(imgView);


                LinearLayout toAddVertical = new LinearLayout(this);
                toAddVertical.setOrientation(LinearLayout.VERTICAL);
                toAddVertical.setLayoutParams(params);
                TextView textView = new TextView(this);

                textView.setTextColor(Color.BLACK);
                textView.setLayoutParams(params);
                textView.setText("Date: "+date);
                toAddVertical.addView(textView);

                textView = new TextView(this);
                textView.setTextColor(Color.BLACK);
                textView.setLayoutParams(params);
                textView.setText("Result: "+result);
                toAddVertical.addView(textView);
                if(accuracy.charAt(accuracy.length()-1) == '\n'){
                    accuracy = accuracy.substring(0, accuracy.length() - 1);
                }
                textView = new TextView(this);
                textView.setTextColor(Color.BLACK);
                textView.setLayoutParams(params);
                textView.setText("Accuracy: "+accuracy+"%");

                toAddVertical.addView(textView);


                toAddHorizontal.addView(toAddVertical);
                linearLayout.addView(toAddHorizontal);

            }
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public BottomNavigationView.OnNavigationItemSelectedListener selectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.miHome:

                    if(fragm != 0){
                        cont.removeAllViews();
                        resetGalleryFragment();
                        cont.addView(root);
                        cont.removeView(manual);
                        cont.removeView(gallery);
                        cont.removeView(disease);
                        cont.removeView(profile);
                        cont.removeView(result);
                        fragm = 0;
                    }
                    break;
                case R.id.miManual:
                    if(fragm != 1){
                        cont.removeAllViews();
                        resetGalleryFragment();
                        cont.addView(manual);
                        cont.removeView(root);
                        cont.removeView(gallery);
                        cont.removeView(disease);
                        cont.removeView(profile);
                        cont.removeView(result);
                        fragm = 1;
                    }

                    break;
                case R.id.miGallery:
                    if(fragm != 2){
                        getGallery();
                        cont.addView(gallery);
                        cont.removeView(disease);
                        cont.removeView(root);
                        cont.removeView(manual);
                        cont.removeView(profile);
                        cont.removeView(result);

                        DiseaseFragment but2 = (DiseaseFragment) getSupportFragmentManager().findFragmentById(R.id.disease_fragment);
                        fragm = 2;
                    }
                    break;
                case R.id.miProfile:

                    if(out_fragm != 3){
                        cont.removeAllViews();
                        resetGalleryFragment();
                        cont.addView(profile);
                        cont.removeView(root);
                        cont.removeView(gallery);
                        cont.removeView(manual);
                        cont.removeView(disease);
                        cont.removeView(result);
                        fragm = 3;
                    }
                    break;
            }
            return true;
        }

    };
    public static BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

        Intent intent = getIntent();


        email = intent.getStringExtra("email");
        gender = intent.getStringExtra("gender");
        age = intent.getStringExtra("age");

        SharedPreferences login;
        login = this.getSharedPreferences("loginInfo", MODE_PRIVATE);
        email = login.getString("email","");
        gender = login.getString("gender","");
        age = login.getString("age","");
        fname = login.getString("fname","");
        lname = login.getString("lname","");

        email = "admin@test.com";
        gender = "0";
        age = "23.07.1998";

        Log.d("fname",fname);
        Log.d("lname",lname);
//        home = new HomeFragment();
        man = new ManualFragment();
        dis = new DiseaseFragment();
        prof = new ProfileFragment();
        // Initializing the fragments

        addFragment(dis);
        addFragment(man);
        addFragment(prof);

        removeFragment(prof);

        cameraButton2 = (FloatingActionButton) findViewById(R.id.cameraFloatingButton);



    }

    public void resetGalleryFragment(){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gallery = inflater.inflate(R.layout.fragment_gallery,null,false);
    }
    public void setHOST(String ip){
        HOST = "http://"+ip;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static void resetOutput(){

            //We are in the output part so we back to the root part if back is pressed
            cont.removeView(result);
            cont.addView(last);
            fragm = lastFrag;

            // We also reset everything from the output fragment
            imgTaken.setImageResource(android.R.color.transparent);

            // Reset the buttons
            sendButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);

            // Reset the text
            resultText.setText("");

            out_fragm = 0;

    }

    private static final int BACK_INTERVAL = 2000;
    private long backPressedTime;
    @Override
    public void onBackPressed() {

        if(out_fragm == 1){
            resetOutput();
        }else{
            if(backPressedTime + BACK_INTERVAL > System.currentTimeMillis()){
                finishAffinity();

                System.exit(0);
            }else{
                Toast.makeText(getBaseContext(),"Press back again in order to exit",Toast.LENGTH_SHORT).show();
                backPressedTime = System.currentTimeMillis();
            }

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
