package com.example.gymarbete.activities.map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gymarbete.R;
import com.example.gymarbete.activities.map.ui.main.MapFragment;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
    }
}