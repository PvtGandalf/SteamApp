package com.example.steamapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steamapp.data.GamesList;
import com.example.steamapp.data.LoadingStatus;

public class GamesActivity extends AppCompatActivity {

    private static final String STEAM_API_KEY = BuildConfig.STEAM_API_KEY;
    private static final String TAG = GamesActivity.class.getSimpleName();

    private RecyclerView searchResultsRV;

    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;

    private SteamGameAdapter steamGameAdapter;

    // lifecycle stuff
    private GameSearchViewModel gameSearchViewModel = null;
    private Toast noGamesToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.searchResultsRV = findViewById(R.id.rv_search_results);

        this.searchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        this.searchResultsRV.setHasFixedSize(true);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);

        this.steamGameAdapter = new SteamGameAdapter();
        this.searchResultsRV.setAdapter(steamGameAdapter);


        this.gameSearchViewModel = new ViewModelProvider(this)
                .get(GameSearchViewModel.class);

        String includeAppInfo = "true";
        gameSearchViewModel.loadGameSearchResults(STEAM_API_KEY, FriendsActivity.CURRENT_PLAYER_STEAM_ID, includeAppInfo);

        this.noGamesToast = Toast.makeText(
                        this,
                        getString(R.string.games_toast),
                        Toast.LENGTH_LONG
                );


        this.gameSearchViewModel.getGameSearchResults().observe(
                this,
                new Observer<GamesList>() {
                    @Override
                    public void onChanged(GamesList GamesList) {
                        steamGameAdapter.updateSearchResults(GamesList);
                        if (steamGameAdapter.getItemCount() == -1) {
                            if (noGamesToast != null) {
                                noGamesToast.cancel();
                            }
                            noGamesToast.show();
                            onBackPressed();
                        }

                    }
                }
        );

        this.gameSearchViewModel.getLoadingStatus().observe(
                this,
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicatorPB.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {

                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);


                        } else {

                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }
}
