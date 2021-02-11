package com.example.gymarbete;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.gymarbete.ui.bluetooth.BLESettings;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.offwhite));
        Intent intent = new Intent(MainActivity.this, DirectContactHandler.class);
        intent.setAction(DirectContactHandler.ACTION_START_FOREGROUND_SERVICE);
        startService(intent);

        setContentView(R.layout.activity_main);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null){
                                    Double lat = location.getAltitude();
                                    Double longt = location.getLongitude();
                                    Log.i("Lat: ",Double.toString(lat));
                                    Log.i("Longt: ",Double.toString(longt));
                                }else{
                                   Log.i("", "No gps");
                                }
                            }
                        });

            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }


    }
    public void openWhitelistFragment(View view) {
        Log.i("Nav", "opening whitelist");
        setContentView(R.layout.fragment_whitelist);
    }
    public void openInfoFragment(View view) {
        Log.i("Nav", "opening info");
        setContentView(R.layout.fragment_info);
    }
    public void openMapFragment(View view) {
        Log.i("Nav", "opening map");
        setContentView(R.layout.fragment_map);
    }
    public void openBLESettingsFragment(View view) {
        Log.i("Nav", "opening whitelist");
        Intent intent = new Intent(MainActivity.this, BLESettings.class);
        MainActivity.this.startActivity(intent);
    }

    private void navigateTo(String fragmentName, String fragmentString) {

    }
}