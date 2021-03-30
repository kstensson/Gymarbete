package com.example.gymarbete.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDateTime;

@Entity
public class Tracker {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "datetime")
    public String dateTime;

    @ColumnInfo(name = "longt")
    public Double longt;

    @ColumnInfo(name = "lat")
    public Double lat;

    public LatLng getLatLng() {
        return new LatLng(lat, longt);
    }

    public Tracker(Double lat, Double longt) {
        LocalDateTime time = LocalDateTime.now();
        this.dateTime = time.toString();
        this.lat = lat;
        this.longt = longt;
    }
}