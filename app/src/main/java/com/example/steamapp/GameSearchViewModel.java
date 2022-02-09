package com.example.steamapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.steamapp.data.GameSearchRepository;
import com.example.steamapp.data.GamesList;
import com.example.steamapp.data.LoadingStatus;

public class GameSearchViewModel extends ViewModel {
    private GameSearchRepository repository;
    private LiveData<GamesList> gameSearchResults;

    private LiveData<LoadingStatus> loadingStatus;

    public GameSearchViewModel(){
        this.repository = new GameSearchRepository();
        this.gameSearchResults = this.repository.getGameSearchResults();
        this.loadingStatus = this.repository.getLoadingStatus();
    }

    public LiveData<GamesList> getGameSearchResults() { return this.gameSearchResults; }
    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadGameSearchResults(String API_KEY, String playerID, String includeAppInfo){
        this.repository.loadGameSearchResults(API_KEY, playerID, includeAppInfo);
    }
}
