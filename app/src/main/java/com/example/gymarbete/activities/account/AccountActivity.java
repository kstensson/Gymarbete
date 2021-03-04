package com.example.gymarbete.activities.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gymarbete.BleGpsService;
import com.example.gymarbete.R;


public class AccountActivity extends AppCompatActivity {
    public static final int REQUEST_ENABLE_BLUETOOTH = 11;
    private static final int REQUEST_ENABLE_COARSE_LOCATION = 1;
    BleGpsService mService;
    boolean mBound = false;
    private ListView devicesList;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private Button scanningBtn;
    private DeviceAdapter listAdapter;
    private final BroadcastReceiver devicesFoundReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() != null && device.getName().contains("armband")) {
                    listAdapter.add(device);
                    listAdapter.notifyDataSetChanged();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

            }
        }
    };
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BleGpsService.LocalBinder binder = (BleGpsService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_settings_activity);

        Context mContext = getApplicationContext();
        Intent intent = new Intent(mContext, BleGpsService.class);
        mContext.bindService(intent,connection, Context.BIND_AUTO_CREATE);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        devicesList = findViewById(R.id.devicesList);
        scanningBtn = findViewById(R.id.scanningBtn);
        listAdapter = new DeviceAdapter(this, R.layout.device_item);
        devicesList.setAdapter(listAdapter);

        checkBluetoothState();
        registerReceiver(devicesFoundReciever, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(devicesFoundReciever, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(devicesFoundReciever, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        scanningBtn.setOnClickListener(v -> {
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                if (checkCoarseLocationPermission()) {
                    listAdapter.clear();
                    bluetoothAdapter.startDiscovery();
                } else {
                    checkBluetoothState();
                }
            }
        });
        checkCoarseLocationPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BleGpsService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(devicesFoundReciever);
    }

    private boolean checkCoarseLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ENABLE_COARSE_LOCATION);
            return false;
        } else {
            return true;
        }
    }

    private void checkBluetoothState() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this,"BLE not supported", Toast.LENGTH_SHORT).show();
        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.isDiscovering()) {
                    Toast.makeText(this, "Device discovering process ...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
                    scanningBtn.setEnabled(true);
                }
            } else {
                Toast.makeText(this, "You need to enable bluetooth", Toast.LENGTH_SHORT).show();
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            checkBluetoothState();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ENABLE_COARSE_LOCATION :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Coarse location allowed, bluetooth scanning should work", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Coarse location forbidden, bluetooth scanning will not work", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void connectToDevice(BluetoothDevice device) {
        device.connectGatt(this,true, mService.gattCallback);
    }
}
