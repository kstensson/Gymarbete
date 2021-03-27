package com.example.gymarbete.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WhitelistID {
    public WhitelistID(int wid) {
        this.wid = wid;
    }

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "whitelist")
    public int wid;
}