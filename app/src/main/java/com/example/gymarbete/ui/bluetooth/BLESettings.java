package com.example.gymarbete.ui.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gymarbete.R;
import com.example.gymarbete.ui.bluetooth.ui.main.BLESettingsFragment;

public class BLESettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_l_e_settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BLESettingsFragment.newInstance())
                    .commitNow();
        }
    }
}