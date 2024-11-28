package com.example.geolocalization_application;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if permissions are granted
        if (!PermissionsHelper.arePermissionsGranted(this)) {
            PermissionsHelper.requestPermissions(this);
        }

        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        Button trackBtn = (Button) findViewById(R.id.trackBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TrackingActivity.class);
                startActivity(i);
            }
        });
    }

    // Handle the permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionsHelper.handlePermissionsResult(requestCode, grantResults)) {
            // Permissions accordées, continuer l'exécution
        } else {
            // Gérer le refus des permissions, peut-être demander de nouveau
            Toast.makeText(this, "Les permissions sont nécessaires pour l'application", Toast.LENGTH_SHORT).show();
        }
    }
}



