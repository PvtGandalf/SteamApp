package com.example.steamapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedPlayersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SavedPlayer player);

    @Delete
    void delete(SavedPlayer player);

    @Query("SELECT * FROM savedPlayers ORDER BY timestamp DESC")
    LiveData<List<SavedPlayer>> getAllSavedPlayers();

    @Query("SELECT * FROM savedPlayers WHERE id = :steamid LIMIT 1")
    LiveData<SavedPlayer> getSavedPlayerByID(String steamid);
}
