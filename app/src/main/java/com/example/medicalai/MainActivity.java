package com.example.medicalai;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.medicalai.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.medicalai.ui.disease.DiseaseFragment.cancelButton;
import static com.example.medicalai.ui.disease.DiseaseFragment.imgTaken;
import static com.example.medicalai.ui.disease.DiseaseFragment.resultText;
import static com.example.medicalai.ui.disease.DiseaseFragment.sendButton;
import static com.example.medicalai.ui.home.HomeFragment.cont;
import static com.example.medicalai.ui.home.HomeFragment.disease;
import static com.example.medicalai.ui.home.HomeFragment.fragm;
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

    public static String email,string,age;


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

    public BottomNavigationView.OnNavigationItemSelectedListener selectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.miHome:

                    if(fragm != 0){
                        cont.removeAllViews();
                        cont.addView(root);
                        cont.removeView(manual);
                        cont.removeView(disease);
                        cont.removeView(profile);
                        cont.removeView(result);
                        fragm = 0;
                    }
                    break;
                case R.id.miManual:
                    if(fragm != 1){
                        cont.addView(manual);
                        cont.removeView(root);
                        cont.removeView(disease);
                        cont.removeView(profile);
                        cont.removeView(result);
                        fragm = 1;
                    }

                    break;
                case R.id.miGallery:
                    if(fragm != 2){
                        resetGallery();

                        cont.addView(disease);
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
                        cont.addView(profile);
                        cont.removeView(root);
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

        String email = intent.getStringExtra("email");
        String gender = intent.getStringExtra("gender");
        String age = intent.getStringExtra("age");

//        home = new HomeFragment();
        man = new ManualFragment();
        dis = new DiseaseFragment();
        prof = new SettingsFragment();
        // Initializing the fragments

        addFragment(dis);
        addFragment(man);
        addFragment(prof);

        removeFragment(prof);

        cameraButton2 = (FloatingActionButton) findViewById(R.id.cameraFloatingButton);




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
