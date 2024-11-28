package com.example.geolocalization_application;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class SearchActivity extends MapActivity {
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search);
        Button myLocationBtn = (Button) findViewById(R.id.myLocation);

        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

        // Initialize the map fragment
        initializeMapFragment(R.id.searchMap);

        // Set up the search functionality
        setupSearchListener();

        // Use the base class method to request the current location
        getLastLocation();

    }

    private void setupSearchListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchForLocation(String query) {
        if (query == null || query.isEmpty()) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }

        // Rechercher l'adresse saisie par l'utilisateur
        Geocoder geocoder = new Geocoder(SearchActivity.this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(query, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                myMap.clear();
                // Ajouter un marqueur et déplacer la caméra
                myMap.addMarker(new MarkerOptions().position(latLng).title(query));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Zoom à 15
            } else {
                Toast.makeText(SearchActivity.this, "No location found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error retrieving location. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}