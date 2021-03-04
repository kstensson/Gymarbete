package com.example.gymarbete;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;

import java.util.List;

public class Database {
    public AppDatabase appDb;

    public Database() {

    }
    @Entity
    public static class WhitelistId {
        public WhitelistId(int id) {
            wid = id;
        }

        @PrimaryKey
        public int uid;

        @ColumnInfo(name = "whitelist")
        public int wid;
    }

    @Entity
    public static class whitelistEnabled {
        @PrimaryKey
        public int uid;

        @ColumnInfo(name = "enable_whitelist")
        public String enable;
    }

    @Dao
    public static interface WhitelistDao {
        @Query("SELECT * FROM whitelistid")
        List<WhitelistId> getAll();

        @Insert
        void insertAll(WhitelistId... whitelistIds);

        @Insert
        void insert(WhitelistId whitelistId);

        @Delete
        void delete(WhitelistId wid);
    }

    @androidx.room.Database(entities = {WhitelistId.class}, version = 1)
    public abstract class AppDatabase extends RoomDatabase {
        public abstract WhitelistDao whitelistDao();
    }
}
