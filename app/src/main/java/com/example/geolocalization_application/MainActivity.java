package com.example.geolocalization_application;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;

import java.io.File;


import java.io.File;

public class MainActivity extends AppCompatActivity {

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the user agent for the app to avoid getting blocked by OSM servers
        Configuration.getInstance().setUserAgentValue("com.example.geolocalization_application");


        // Set the default cache directory for map tiles
        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));

        setContentView(R.layout.activity_main);

        // Find the MapView
        map = findViewById(R.id.map);

        // Enable zoom controls and gestures
        map.setMultiTouchControls(true);

        // Set default zoom and location
        map.getController().setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(48.858844, 2.294351); // Example: Eiffel Tower
        map.getController().setCenter(startPoint);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cleanup resources
        if (map != null) {
            map.onDetach();
        }
    }
}
