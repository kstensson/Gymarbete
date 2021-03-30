package com.example.gymarbete.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gymarbete.database.entities.WhitelistEnabled;
import com.example.gymarbete.database.entities.WhitelistID;

import java.util.List;

@Dao
public interface WhitelistDao {
    @Query("SELECT * FROM whitelistid")
    List<WhitelistID> getAll();

    @Insert
    void insertAll(WhitelistID... whitelistIDs);

    @Insert
    void insert(WhitelistID whitelistID);

    @Delete
    void delete(WhitelistID wid);

    @Insert
    void insert(WhitelistEnabled whitelistEnabled);

    @Query("SELECT * FROM whitelistenabled WHERE uid=0")
    WhitelistEnabled getEnabled();

    @Update
    void update(WhitelistEnabled whitelistEnabled);
}
