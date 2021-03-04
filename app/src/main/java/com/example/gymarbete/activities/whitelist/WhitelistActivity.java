package com.example.gymarbete.activities.whitelist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymarbete.BleGpsService;
import com.example.gymarbete.R;
import com.example.gymarbete.activities.whitelist.main.WhitelistFragment;

import java.math.BigInteger;
import java.util.ArrayList;

import kotlin.UInt;

public class WhitelistActivity extends AppCompatActivity {

    public static ArrayList<String> whitelistIDs = new ArrayList<>();
    BleGpsService mService;
    boolean mBound = false;
    private Switch sw;
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
        setContentView(R.layout.whitelist_activity);

        Context mContext = getApplicationContext();
        Intent intent = new Intent(mContext, BleGpsService.class);
        mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WhitelistFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        sw = findViewById(R.id.switchwhitelist);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.whitelistScrollView).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.whitelistScrollView).setVisibility(View.INVISIBLE);
                }
            }
        });

        LinearLayout listView = (LinearLayout) findViewById(R.id.whitelistList);
        whitelistIDs.forEach((entry) -> {
            TextView textView = new TextView(this);
            textView.setText(entry);
            listView.addView(textView);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BleGpsService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void addWhitelistID(View view) {
        TextView entry = new TextView(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle("Add whitelist ID");
        alert.setMessage("Enter the ID from the other users info page");
        final EditText inputText = new EditText(this);
        alert.setView(inputText);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = inputText.getText().toString();

                whitelistIDs.add(input);
                entry.setText(input);
                int value;
                try {
                    value = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "No chracters can be entered as ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                mService.whitelistGattCurrId.setValue(value, BluetoothGattCharacteristic.FORMAT_SINT32, 0);
                mService.bluetoothGatt.writeCharacteristic(mService.whitelistGattCurrId);
                LinearLayout listView = (LinearLayout) findViewById(R.id.whitelistList);
                listView.addView(entry);
            }
        });
        alert.show();


    }
}