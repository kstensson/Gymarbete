package com.example.gymarbete;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymarbete.activities.account.AccountActivity;
import com.example.gymarbete.activities.info.InfoActivity;
import com.example.gymarbete.activities.map.MapActivity;
import com.example.gymarbete.activities.whitelist.WhitelistActivity;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_ENABLE_BT = 0;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.offwhite));
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        Intent intent = new Intent(MainActivity.this, BleGpsService.class);
        startService(intent);
        setContentView(R.layout.fragment_home);
    }

    public void openWhitelistActivity(View view) {
        Intent intent = new Intent(MainActivity.this, WhitelistActivity.class);
        startActivity(intent);
    }
    public void openInfoActivity(View view) {
        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(intent);
    }
    public void openMapActivity(View view) {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }
    public void openBLESettingsActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(intent);
    }
}
