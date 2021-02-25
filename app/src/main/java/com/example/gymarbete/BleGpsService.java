package com.example.gymarbete;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.Random;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import no.nordicsemi.android.ble.BleManager;

public class BleGpsService extends Service {
    private int notid;

    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        getLatLongt();
                    }
                },
                5000
        );

        Random random = new Random();
        notid = random.nextInt(9999);

        Intent notificationIntent = new Intent(this, BleGpsService.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);


        Notification notification =
                new Notification.Builder(this, getString(R.string.CHANNEL_ID))
                        .setContentTitle("BLEGPS service")
                        .setContentText("empty")
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentIntent(pendingIntent)
                        .build();

// Notification ID cannot be 0.
        startForeground(notid, notification);
    }

    private class BleHandler extends BleManager {

        public BleHandler(@NonNull Context context, @NonNull Handler handler) {
            super(context, handler);
        }

        @NonNull
        @Override
        protected BleManagerGattCallback getGattCallback() {
            return null;
        }
    }

    public void getLatLongt() {
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                double lat = location.getAltitude();
                                double longt = location.getLatitude();

                                Log.i("Lat: ", Double.toString(lat));
                                Log.i("Longt: ", Double.toString(longt));
                            } else {
                                Log.i("", "Gps failed");
                            }
                        });
            } /*else {
                ActivityCompat.requestPermissions(,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }*/
        }
    }
}
