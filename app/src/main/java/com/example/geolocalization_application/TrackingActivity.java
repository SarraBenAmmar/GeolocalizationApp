package com.example.geolocalization_application;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class TrackingActivity extends MapActivity {
    private static final int LOCATION_UPDATE_INTERVAL = 1000; // 1 second
    private static final float LOCATION_UPDATE_DISTANCE = 1f; // 1 meter
    private LocationCallback locationCallback;
    private Polyline pathLine;
    private List<LatLng> pathPoints = new ArrayList<>();
    private Location previousLocation;
    private float totalDistance = 0; // Total distance in meters
    private TextView distanceTextView; // TextView to display distance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        // Initialize the map fragment
        initializeMapFragment(R.id.trackingMap);

        distanceTextView = findViewById(R.id.distanceTextView);
        // Start location updates
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        // Check location permissions
        if (!PermissionsHelper.arePermissionsGranted(this)) {
            PermissionsHelper.requestPermissions(this);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(LOCATION_UPDATE_DISTANCE);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && myMap != null) {
                    for (Location location : locationResult.getLocations()) {
                        updateTracking(location);
                    }
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void updateTracking(Location location) {
        // Update map location and path
        currentLocation = location;
        updateMapLocation();

        // Add the new point to the path
        pathPoints.add(new LatLng(location.getLatitude(), location.getLongitude()));

        // Update the polyline on the map
        if (pathLine != null) {
            pathLine.remove();
        }
        pathLine = myMap.addPolyline(new PolylineOptions()
                .addAll(pathPoints)
                .width(10)
                .color(Color.BLUE)); // Customize width and color

        // Calculate and update the distance
        if (previousLocation != null) {
            float distance = previousLocation.distanceTo(location); // Distance in meters
            totalDistance += distance; // Accumulate the distance
            updateDistanceDisplay(totalDistance); // Update the TextView
        }

        // Update the previous location
        previousLocation = location;
    }

    private void updateDistanceDisplay(float distance) {
        // Convert meters to kilometers and update the TextView
        String distanceText = String.format("Distance traveled: %0.f meters", distance);
        distanceTextView.setText(distanceText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}