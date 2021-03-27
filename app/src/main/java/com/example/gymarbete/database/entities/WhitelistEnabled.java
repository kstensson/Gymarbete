package com.example.gymarbete.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WhitelistEnabled {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "enable_whitelist")
    public Boolean enable;

    public WhitelistEnabled(Boolean enable) {
        this.enable = enable;
    }
}