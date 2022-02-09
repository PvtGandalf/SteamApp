package com.example.steamapp.data;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendSearchRepository {
    private static final String TAG = SteamSearchRepository.class.getSimpleName();
    private static final String STEAM_BASE_URL = "https://api.steampowered.com";

    private MutableLiveData<FriendsList> friendsList;
    private MutableLiveData<LoadingStatus> loadingStatus;

    private String currentPlayerID;

    private SteamService steamService;

    public FriendSearchRepository(){
        this.friendsList = new MutableLiveData<>();
        this.friendsList.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FriendInfo.class, new FriendInfo.JsonDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(STEAM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.steamService = retrofit.create(SteamService.class); // inflate it
    }

    public LiveData<FriendsList> getFriendSearchResults() { return this.friendsList; }
    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    // let's do some API calls
    public void loadFriendSearchResults(String API_KEY, String playerID){
        if (shouldExecuteSearch(playerID)) {
            this.currentPlayerID = playerID;

            this.friendsList.setValue(null); // wipe away old results
            this.loadingStatus.setValue(LoadingStatus.LOADING);
            Log.d(TAG, "running new friend search for this playerID: " + playerID);

            Call<FriendsList> results = this.steamService.searchFriends(playerID, API_KEY);

            results.enqueue(new Callback<FriendsList>() {
                @Override
                public void onResponse(Call<FriendsList> call, Response<FriendsList> response) {
                    if (response.code() == 200) {
                        Log.d(TAG, "response data: " + response.body());
                        Log.d(TAG, "response code:: " + response.code());

                        friendsList.setValue(response.body());
                        loadingStatus.setValue(LoadingStatus.SUCCESS);

                        Log.d(TAG, "SUCCESSful API request: " + call.request().url());
                    } else {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                        Log.d(TAG, "error response code: " + response.code());
                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
                    }
                }

                @Override
                public void onFailure(Call<FriendsList> call, Throwable t) {
                    t.printStackTrace();
                    loadingStatus.setValue(LoadingStatus.ERROR);
                }
            });
        } else {
            Log.d(TAG, "using cached results for this query: " + playerID );
            loadingStatus.setValue(LoadingStatus.SUCCESS);
        }
    }

    private boolean shouldExecuteSearch(String playerID){
        return !TextUtils.equals(playerID, this.currentPlayerID)
                || this.loadingStatus.getValue() == LoadingStatus.ERROR;
    }
}
