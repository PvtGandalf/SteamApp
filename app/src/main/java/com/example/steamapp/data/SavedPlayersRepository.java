package com.example.steamapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SavedPlayersRepository {
    private SavedPlayersDao dao;

    public SavedPlayersRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.dao = db.savedPlayersDao();
    }

    public void insertSavedPlayer(SavedPlayer player) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(player);
            }
        });
    }

    public void deleteSavedPlayer(SavedPlayer player) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(player);
            }
        });
    }

    public LiveData<List<SavedPlayer>> getAllSavedPlayers(){
        return dao.getAllSavedPlayers();
    }

    public LiveData<SavedPlayer> getSavedPlayerByID(String steamid){
        return this.dao.getSavedPlayerByID(steamid);
    }

}
