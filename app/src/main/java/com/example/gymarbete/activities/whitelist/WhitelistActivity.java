package com.example.gymarbete.activities.whitelist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.gymarbete.R;
import com.example.gymarbete.activities.whitelist.main.WhitelistFragment;

import java.util.ArrayList;

public class WhitelistActivity extends AppCompatActivity {

    public static ArrayList<String> whitelistIDs = new ArrayList<>();

    private Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whitelist_activity);

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
                LinearLayout listView = (LinearLayout) findViewById(R.id.whitelistList);
                listView.addView(entry);
            }
        });
        alert.show();


    }
}