package com.example.gymarbete.activities.whitelist;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gymarbete.R;
import com.example.gymarbete.activities.account.AccountActivity;
import com.example.gymarbete.database.entities.WhitelistID;

public class WhitelistAdapter extends ArrayAdapter<WhitelistID> {
    private final WhitelistActivity mContext;
    public WhitelistAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = (WhitelistActivity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WhitelistID id = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.whitelistitem, parent, false);
        }

        TextView name = convertView.findViewById(R.id.whitelist_item_name);

        name.setText(id.name);

        return convertView;
    }
}
