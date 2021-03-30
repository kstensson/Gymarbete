package com.example.gymarbete.activities.account;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymarbete.R;

public class DeviceAdapter extends ArrayAdapter<BluetoothDevice> {
    private final AccountActivity mContext;
    public DeviceAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = (AccountActivity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BluetoothDevice device = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView uuid = convertView.findViewById(R.id.uuid);

        Button button = convertView.findViewById(R.id.connectBtn);
        button.setOnClickListener(v -> {
            mContext.connectToDevice(device);

        });
        name.setText(device.getName());
        uuid.setText(device.getAddress());

        return convertView;
    }
}
