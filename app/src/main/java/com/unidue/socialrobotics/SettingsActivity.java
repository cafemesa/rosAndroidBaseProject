package com.unidue.socialrobotics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("SocialRobotics - Settings");


        EditText edMasterIP = findViewById(R.id.edMasterIP);
        edMasterIP.setText(LoginActivity.sharedPref.getString("MasterIP",""));
        EditText etMasterPort = findViewById(R.id.etMasterPort);
        etMasterPort.setText(LoginActivity.sharedPref.getString("MasterPort",""));
        Button btnSaveParameters = findViewById(R.id.btnSaveParameters);

        btnSaveParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edMasterIP.getText().toString().equals("") || etMasterPort.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Complete the fields", Toast.LENGTH_LONG).show();
                }
                else
                {
                    LoginActivity.editor.putString("MasterIP", edMasterIP.getText().toString());
                    LoginActivity.editor.putString("MasterPort", etMasterPort.getText().toString());
                    LoginActivity.editor.apply();
                    onBackPressed();
                }
            }
        });

    }
}