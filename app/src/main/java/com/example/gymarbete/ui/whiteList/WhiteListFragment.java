package com.example.gymarbete.ui.whiteList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gymarbete.R;

public class WhiteListFragment extends Fragment {

    private WhiteListModel whiteListModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        whiteListModel =
                new ViewModelProvider(this).get(WhiteListModel.class);
        View root = inflater.inflate(R.layout.fragment_whitelist, container, false);
        final TextView textView = root.findViewById(R.id.text_whitelist);
        whiteListModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
    
}