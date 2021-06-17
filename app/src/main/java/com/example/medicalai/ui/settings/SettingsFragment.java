package com.example.medicalai.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medicalai.MainActivity;
import com.example.medicalai.R;

public class SettingsFragment extends Fragment {
    private Button ipButton;
    private EditText editText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        Context con = getContext();

        ipButton = root.findViewById(R.id.getIPButton);
        editText = root.findViewById(R.id.newIp);
        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newIP = editText.getText().toString();
                changeHost(newIP);
            }
        });


        return root;
    }

    private void changeHost(String ip){
        ((MainActivity)this.getActivity()).setHOST(ip);
    }
}