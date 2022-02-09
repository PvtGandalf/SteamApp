package com.example.steamapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.steamapp.data.FriendSearchRepository;
import com.example.steamapp.data.FriendsList;
import com.example.steamapp.data.LoadingStatus;

public class FriendSearchViewModel extends ViewModel {
    private FriendSearchRepository repository;
    private LiveData<FriendsList> friendSearchResults;

    private LiveData<LoadingStatus> loadingStatus;

    public FriendSearchViewModel(){
        this.repository = new FriendSearchRepository();
        this.friendSearchResults = this.repository.getFriendSearchResults();
        this.loadingStatus = this.repository.getLoadingStatus();
    }

    public LiveData<FriendsList> getFriendSearchResults() { return this.friendSearchResults; }
    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadFriendSearchResults(String API_KEY, String playerID){
        this.repository.loadFriendSearchResults(API_KEY, playerID);
    }
}
