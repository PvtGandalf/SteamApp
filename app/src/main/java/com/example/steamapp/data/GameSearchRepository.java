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

public class GameSearchRepository {
    private static final String TAG = SteamSearchRepository.class.getSimpleName();
    private static final String STEAM_BASE_URL = "https://api.steampowered.com";

    private MutableLiveData<GamesList> gamesList;
    private MutableLiveData<LoadingStatus> loadingStatus;

    private String currentPlayerID;

    private SteamService steamService;

    public GameSearchRepository(){
        this.gamesList = new MutableLiveData<>();
        this.gamesList.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GameInfo.class, new GameInfo.JsonDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(STEAM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.steamService = retrofit.create(SteamService.class); // inflate it
    }

    public LiveData<GamesList> getGameSearchResults() { return this.gamesList; }
    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    // let's do some API calls
    public void loadGameSearchResults(String API_KEY, String playerID, String includeAppInfo){
        if (shouldExecuteSearch(playerID)) {
            this.currentPlayerID = playerID;

            this.gamesList.setValue(null); // wipe away old results
            this.loadingStatus.setValue(LoadingStatus.LOADING);
            Log.d(TAG, "running new game search for this playerID: " + playerID);

            Call<GamesList> results = this.steamService.searchGames(includeAppInfo, playerID, API_KEY);

            results.enqueue(new Callback<GamesList>() {
                @Override
                public void onResponse(Call<GamesList> call, Response<GamesList> response) {
                    if (response.code() == 200) {
                        Log.d(TAG, "response data: " + response.body());
                        Log.d(TAG, "response code:: " + response.code());

                        Log.d(TAG, "GameSearchRepository SUCCESSful API request: " + call.request().url());

                        gamesList.setValue(response.body());
                        loadingStatus.setValue(LoadingStatus.SUCCESS);


                    } else {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                        Log.d(TAG, "error response code: " + response.code());
                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
                    }
                }

                @Override
                public void onFailure(Call<GamesList> call, Throwable t) {
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
