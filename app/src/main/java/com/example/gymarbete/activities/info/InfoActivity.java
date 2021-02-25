package com.example.gymarbete.activities.info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gymarbete.R;
import com.example.gymarbete.activities.info.ui.main.InfoFragment;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, InfoFragment.newInstance())
                    .commitNow();
        }
    }
}