package com.example.gymarbete.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gymarbete.database.entities.Tracker;
import com.example.gymarbete.database.entities.WhitelistID;

import java.util.List;

@Dao
public interface TrackerDao {
    @Query("SELECT * FROM tracker")
    List<Tracker> getAll();

    @Insert
    void insertAll(Tracker... trackers);

    @Insert
    void insert(Tracker tracker);

    @Delete
    void delete(Tracker tracker);
}
