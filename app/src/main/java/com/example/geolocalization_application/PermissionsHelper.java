package com.example.geolocalization_application;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsHelper {

    public static final int PERMISSION_REQUEST_CODE = 100;

    // Liste des permissions nécessaires
    private static final String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.INTERNET
    };

     // Vérifie si toutes les permissions nécessaires sont accordées.
    public static boolean arePermissionsGranted(Context context) {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


     // Demande les permissions manquantes.
    public static void requestPermissions(Activity activity) {
        List<String> missingPermissions = new ArrayList<>();
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    activity,
                    missingPermissions.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    // Gère le résultat des demandes de permissions.
    public static boolean handlePermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    //Vérifie si la localisation est activée.
    public static boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // For Android 9 (Pie) and later
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isLocationEnabled();
        } else {
        // For Android versions below 9
        int mode = Settings.Secure.getInt(
                context.getContentResolver(),
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
        );
        return (mode != Settings.Secure.LOCATION_MODE_OFF);
    }
    }


    // Propose un Dialog pour activer la localisation si elle est désactivée.
    public static void promptEnableLocation(final Activity activity) {
        if (!isLocationEnabled(activity)) {
            // Créer un AlertDialog pour donner le choix à l'utilisateur
            new AlertDialog.Builder(activity)
                    .setTitle("Activation de la localisation")
                    .setMessage("La localisation est désactivée. Voulez-vous l'activer ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Ouvrir les paramètres de localisation
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // L'utilisateur a choisi de ne pas activer la localisation
                            Toast.makeText(activity, "Vous ne pouvez pas utiliser cette application sans la localisation.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    }

    // Vérifie si la connexion Internet est disponible.
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    // Propose un Dialog pour activer la connexion Internet si elle est désactivée.
    public static void promptEnableInternet(final Activity activity) {
        if (!isInternetAvailable(activity)) {
            // Créer un AlertDialog pour donner le choix à l'utilisateur
            new AlertDialog.Builder(activity)
                    .setTitle("Activation de la connexion Internet")
                    .setMessage("La connexion Internet est désactivée. Voulez-vous l'activer ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Ouvrir les paramètres de localisation
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            activity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // L'utilisateur a choisi de ne pas activer la localisation
                            Toast.makeText(activity, "Vous ne pouvez pas utiliser cette application sans une connexion Internet.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    }
}
