package com.example.steamapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.steamapp.data.SavedPlayer;
import com.example.steamapp.data.SavedPlayersRepository;

import java.util.List;

public class SavedPlayersViewModel extends AndroidViewModel {
    private SavedPlayersRepository repository;

    public SavedPlayersViewModel(Application application){
        super(application);
        this.repository = new SavedPlayersRepository(application);
    }

    public void insertPlayer(SavedPlayer player){
        this.repository.insertSavedPlayer(player);
    }

    public void deletePlayer(SavedPlayer player){
        this.repository.deleteSavedPlayer(player);
    }

    public LiveData<List<SavedPlayer>> getAllSavedPlayers(){
        return repository.getAllSavedPlayers();
    }

    public LiveData <SavedPlayer> getSavedPlayerBySteamID(String steamid){
        return this.repository.getSavedPlayerByID(steamid);
    }
}
