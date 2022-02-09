package com.example.steamapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.steamapp.data.LoadingStatus;
import com.example.steamapp.data.PlayerData;
import com.example.steamapp.data.SteamSearchRepository;

public class SteamSearchViewModel extends ViewModel {
    private SteamSearchRepository repository;
    private LiveData<PlayerData> playerSearchResults;

    private LiveData<LoadingStatus> loadingStatus;

    public SteamSearchViewModel(){
        this.repository = new SteamSearchRepository();
        this.playerSearchResults = this.repository.getPlayerSearchResults();
        this.loadingStatus = this.repository.getLoadingStatus();
    }

    public LiveData<PlayerData> getPlayerSearchResults() { return this.playerSearchResults; }
    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadPlayerSearchResults(String API_KEY, String playerID){
        this.repository.loadPlayerSearchResults(API_KEY, playerID);
    }
}
