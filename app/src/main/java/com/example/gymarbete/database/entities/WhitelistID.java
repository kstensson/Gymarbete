package com.example.gymarbete.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class WhitelistID {
    @Ignore
    public WhitelistID(int wid) {
        this.wid = wid;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "whitelist")
    public int wid;

    @ColumnInfo(name = "name")
    public String name;

    public WhitelistID(int wid, String name) {
        this.wid = wid;
        this.name = name;
    }
}