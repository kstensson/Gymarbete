package com.example.gymarbete;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Random;
import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;

public class BleGpsService extends Service {
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString("19B10001-E8F2-537E-4F6C-D104768A1214");
    private final static String TAG = BleGpsService.class.getSimpleName();
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private final IBinder binder = new LocalBinder();
    private int notid;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private String bluetoothDeviceAddress;
    public BluetoothGatt bluetoothGatt;
    private BluetoothGattService whitelistGattService;
    public BluetoothGattCharacteristic whitelistGattCurrId;
    public BluetoothGattCharacteristic whitelistGattNext;
    private int connectionState = STATE_DISCONNECTED;

    // W for whitelist
    public Database.AppDatabase wDb;
    public Database.WhitelistDao wDao;

    public final BluetoothGattCallback gattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    //String intentAction;
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        /*intentAction = ACTION_GATT_CONNECTED;
                        connectionState = STATE_CONNECTED;
                        broadcastUpdate(intentAction);*/
                        bluetoothGatt = gatt;
                        Log.i(TAG, "Connected to GATT server.");
                        Log.i(TAG, "Attempting to start service discovery:" +
                                bluetoothGatt.discoverServices());

                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        // intentAction = ACTION_GATT_DISCONNECTED;
                        connectionState = STATE_DISCONNECTED;
                        Log.i(TAG, "Disconnected from GATT server.");
                        // broadcastUpdate(intentAction);
                    }
                }

                @Override
                // New services discovered
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        whitelistGattService = gatt.getService(UUID.fromString("19B10000-E8F2-537E-4F6C-D104768A1214"));
                        whitelistGattCurrId = whitelistGattService.getCharacteristic(UUID.fromString("19B10001-E8F2-537E-4F6C-D104768A1214"));
                        whitelistGattNext = whitelistGattService.getCharacteristic(UUID.fromString("790818f1-6d2e-4aa1-95d2-3738c8ee1591"));

                         Log.i("ble",whitelistGattNext.getStringValue(0));
                    } else {
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                @Override
                // Result of a characteristic read operation
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        // broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                    }
                }
            };

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

        wDb = Room.databaseBuilder(getApplicationContext(), Database.AppDatabase.class, "db1").build();
        wDao = wDb.whitelistDao();


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

    public void testDao(int id ) {
        wDao.insert(new Database.WhitelistId(id));
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

    /*private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            final String heartRate = characteristic.getStringValue(1);
            Log.d(TAG, String.format("Data %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else if(UUID_) {

        } else {

        }
        sendBroadcast(intent);
    }*/
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
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

    public class LocalBinder extends Binder {
        public BleGpsService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BleGpsService.this;
        }
    }


}
