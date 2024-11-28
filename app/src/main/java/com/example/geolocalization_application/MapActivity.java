package com.example.geolocalization_application;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    protected FusedLocationProviderClient fusedLocationClient;
    protected Location currentLocation;
    protected GoogleMap myMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionsHelper.promptEnableInternet(this);
        PermissionsHelper.promptEnableLocation(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    protected void initializeMapFragment(int mapFragmentId) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(mapFragmentId);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // Récupérer la dernière localisation de l'utilisateur
    protected void getLastLocation() {
        if (!PermissionsHelper.arePermissionsGranted(this)) {
            return;
        }
        // Récupérer la dernière localisation
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    if (myMap != null) {
                        updateMapLocation();
                    }
                } else {
                    Toast.makeText(MapActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Si la localisation est dejà disponible, on peut mettre à jour la carte
        if (currentLocation != null) {
            updateMapLocation();
        }
    }

    protected void updateMapLocation() {
        // Si currentLocation n'est pas null, on ajoute un marqueur et deplace la caméra
        LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        myMap.clear();
        myMap.addMarker(new MarkerOptions().position(myLocation).title("my location"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15)); // Zoom
    }
}