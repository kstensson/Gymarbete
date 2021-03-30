package com.example.gymarbete.database;


import androidx.room.RoomDatabase;

import com.example.gymarbete.database.dao.TrackerDao;
import com.example.gymarbete.database.dao.WhitelistDao;
import com.example.gymarbete.database.entities.Tracker;
import com.example.gymarbete.database.entities.WhitelistEnabled;
import com.example.gymarbete.database.entities.WhitelistID;

@androidx.room.Database(entities = {WhitelistID.class,  WhitelistEnabled.class, Tracker.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WhitelistDao whitelistDao();
    public abstract TrackerDao trackerDao();
}