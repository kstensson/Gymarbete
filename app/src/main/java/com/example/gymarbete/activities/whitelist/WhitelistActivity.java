package com.example.gymarbete.activities.whitelist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymarbete._Service;
import com.example.gymarbete.R;
import com.example.gymarbete.activities.whitelist.main.WhitelistFragment;
import com.example.gymarbete.database.entities.WhitelistID;

import java.util.ArrayList;

public class WhitelistActivity extends AppCompatActivity {

    public static WhitelistAdapter whitelistIDs;
    _Service mService;
    boolean mBound = false;
    boolean firstcreation = true;

    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            _Service.LocalBinder binder = (_Service.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            whitelistIDs.addAll(mService.whitelistIDS);
            firstcreation = false;
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

        whitelistIDs = new WhitelistAdapter(this, R.layout.whitelistitem);

        Intent intent = new Intent(mContext, _Service.class);
        mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);

        /* Gjort under stress. Är väl medveten om snyggare metoder
         Något med asykroniteten i åtkomsten till databasen gör listan ofta tom
         med mer tid hade jag implementerat att den skickar en signal när åtkomsten
         är färdig istället för att brute-forcea det såhär */
        if (!firstcreation)
            whitelistIDs.addAll(mService.whitelistIDS);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WhitelistFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch sw = findViewById(R.id.switchwhitelist);
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                findViewById(R.id.whitelistScrollView).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.whitelistScrollView).setVisibility(View.INVISIBLE);
            }
        });

        ListView listView = findViewById(R.id.whitelist_listview);
        listView.setAdapter(whitelistIDs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, _Service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void addWhitelistID(View view) {
        TextView entry = new TextView(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle("Add whitelist ID");
        alert.setMessage("Enter the ID from the other users info page");

        LayoutInflater inflater = getLayoutInflater();
        View inputDialog = inflater.inflate(R.layout.whitelistdialog, null);

        alert.setView(inputDialog);
        // TODO: add option for name
        alert.setPositiveButton("Add", (dialog, which) -> {

            EditText idInput = inputDialog.findViewById(R.id.id_input);
            String id = idInput.getText().toString();
            EditText nameInput = inputDialog.findViewById(R.id.name_input);
            String name = nameInput.getText().toString();

            int value;
            try {
                value = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "No chracters can be entered as ID", Toast.LENGTH_SHORT).show();
                return;
            }
            WhitelistID whitelistID = new WhitelistID(value, name);
            whitelistIDs.add(whitelistID);

            if (mService.isConnectedBLE()) {
                mService.whitelistGattCurrId.setValue(value, BluetoothGattCharacteristic.FORMAT_SINT32, 0);
                mService.bluetoothGatt.writeCharacteristic(mService.whitelistGattCurrId);
            }
            /*LinearLayout listView = findViewById(R.id.whitelist_list);
            listView.addView(entry);

            WhitelistID whitelistId = new WhitelistID(value);*/
            new Thread(() -> mService.wDao.insert(whitelistID)).start();

        });
        alert.show();
    }

    public float dpToPixel(float dp) {
        return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}