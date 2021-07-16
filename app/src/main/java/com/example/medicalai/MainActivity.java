package com.example.medicalai;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.medicalai.ui.disease.DiseaseFragment.cameraButton;
import static com.example.medicalai.ui.disease.DiseaseFragment.cancelButton;
import static com.example.medicalai.ui.disease.DiseaseFragment.cont;
import static com.example.medicalai.ui.disease.DiseaseFragment.fragm;
import static com.example.medicalai.ui.disease.DiseaseFragment.imgTaken;
import static com.example.medicalai.ui.disease.DiseaseFragment.manual;
import static com.example.medicalai.ui.disease.DiseaseFragment.out_fragm;
import static com.example.medicalai.ui.disease.DiseaseFragment.profile;
import static com.example.medicalai.ui.disease.DiseaseFragment.result;
import static com.example.medicalai.ui.disease.DiseaseFragment.resultText;
import static com.example.medicalai.ui.disease.DiseaseFragment.root;
import static com.example.medicalai.ui.disease.DiseaseFragment.sendButton;
import static com.example.medicalai.ui.disease.DiseaseFragment.uploadButton;

// Importing the contents from the DiseaseFragment so we can reset it

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    public static String HOST = "http://192.168.100.2:5000";
    public static FloatingActionButton cameraButton2;

    public BottomNavigationView.OnNavigationItemSelectedListener selectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.miHome:
                    Log.d("Nav","Home");
                    if(fragm != 0){
                        cont.addView(root);
                        cont.removeView(manual);
                        cont.removeView(profile);
                        fragm = 0;
                    }
                    break;
                case R.id.miManual:
                    Log.d("Nav","miManual");
                    if(fragm != 1){
                        cont.addView(manual);
                        cont.removeView(root);
                        cont.removeView(profile);
                        fragm = 1;
                    }

                    break;
                case R.id.miGallery:
                    if(fragm != 2){
                        cont.addView(root);
                        cont.removeView(manual);
                        cont.removeView(profile);
                        fragm = 2;
                    }
                    break;
                case R.id.miProfile:
                    if(out_fragm != 3){
                        cont.addView(profile);
                        cont.removeView(manual);
                        cont.removeView(root);
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


    @Override
    public void onBackPressed() {
        if(out_fragm == 1){
            // We are in the output part so we back to the root part if back is pressed
            cont.removeView(result);
            cont.addView(root);

            // We also reset everything from the output fragment
            imgTaken.setImageResource(android.R.color.transparent);

            // Reset the buttons
            sendButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            cameraButton.setVisibility(View.VISIBLE);
            uploadButton.setVisibility(View.VISIBLE);

            // Reset the text
            resultText.setText("");

            out_fragm = 0;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
