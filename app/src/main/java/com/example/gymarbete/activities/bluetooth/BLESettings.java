package com.example.gymarbete.activities.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gymarbete.R;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


public class BLESettings extends AppCompatActivity {
    private RxBleClient rxBleClient;
    private RxBleDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxBleClient = RxBleClient.create(this);
        setContentView(R.layout.ble_settings_activity);
    }

    public void scan(View view) {
        Disposable flowDisposable = rxBleClient.scanBleDevices(new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_OPPORTUNISTIC)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                // change if needed
                .build())
                .subscribe(
                        rxBleScanResult -> {
                            Log.i("BLE device", rxBleScanResult.getBleDevice().getName());
                            if (rxBleScanResult.getBleDevice().getName().contains("armband")) {
                                device = rxBleScanResult.getBleDevice();
                            }
                        },
                        throwable -> {
                            Log.w("BLE", throwable.getMessage());
                        }
                );
        flowDisposable.dispose();
        if (device != null) {
            Disposable disposable = device.establishConnection(true) // <-- autoConnect flag
                    .subscribe(
                            rxBleConnection -> {
                                // All GATT operations are done through the rxBleConnection.

                            },
                            throwable -> {
                                Log.w("BLE", throwable.getMessage());
                            }
                    );
            // When done... dispose and forget about connection teardown :)
            disposable.dispose();

            device.establishConnection(false)
                    .flatMap(rxBleConnection -> rxBleConnection.setupNotification(UUID.fromString("19B10000-E8F2-537E-4F6C-D104768A1214")))
                    .doOnNext(notificationObservable -> {
                        // Notification has been set up
                    })
                    .flatMap(notificationObservable -> notificationObservable) // <-- Notification has been set up, now observe value changes.
                    .subscribe(
                            bytes -> {
                                // Given characteristic has been changes, here is the value.
                            },
                            throwable -> {
                                Log.w("BLE", throwable.getMessage());

                            }
                    );
        } else {
            Log.w("BLE", "Device not found");
        }
    }
}