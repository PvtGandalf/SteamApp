package com.example.steamapp.data;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "savedPlayers")
public class SavedPlayer {
    @PrimaryKey
    @NonNull
    public String id;

   // @NonNull
   // public String name;

    @NonNull
    public long timestamp;

    public SavedPlayer(String id, long timestamp){
        this.id = id;
     //   this.name = name;
        this.timestamp = timestamp;
    }
}
