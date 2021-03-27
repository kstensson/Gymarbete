package com.example.gymarbete.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gymarbete.database.entities.WhitelistID;

import java.util.List;

@Dao
public interface WhitelistDao {
    @Query("SELECT * FROM whitelistid")
    List<WhitelistID> getAll();

    @Insert
    void insertAll(WhitelistID... whitelistIds);

    @Insert
    void insert(WhitelistID whitelistId);

    @Delete
    void delete(WhitelistID wid);
}
