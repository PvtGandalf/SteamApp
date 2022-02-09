package com.example.steamapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steamapp.data.LoadingStatus;
import com.example.steamapp.data.PlayerData;
import com.example.steamapp.data.PlayerSummary;
import com.example.steamapp.data.SavedPlayer;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements  SharedPreferences.OnSharedPreferenceChangeListener, NavigationView.OnNavigationItemSelectedListener,
        SteamPlayerAdapter.OnPlayerClickListener, SavedPlayerAdapter.OnSearchResultClickedListener {


    private static final String STEAM_API_KEY = BuildConfig.STEAM_API_KEY;
    private static final String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences sharedPreferences;
    private RecyclerView searchResultsRV;
    private EditText searchBoxET;

    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;

  //  private SharedPreferences sharedPreferences;


    private DrawerLayout drawerLayout;
    private RecyclerView drawerRV;
    private SteamPlayerAdapter steamPlayerAdapter;
    private SavedPlayerAdapter savedPlayerAdapter;

    // this data structure is used in the player activity
    private PlayerSummary playerSummary;

    // lifecycle stuff
    private SteamSearchViewModel steamSearchViewModel;
    private SavedPlayersViewModel savedPlayersViewModel;

    private Toast searchToast;

    private FriendsActivity friendsActivity = null;

    public int myPersonaStateInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.searchBoxET = findViewById(R.id.et_search_box);
        this.searchResultsRV = findViewById(R.id.rv_search_results);

        this.searchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        this.searchResultsRV.setHasFixedSize(true);

        this.drawerLayout = findViewById(R.id.drawer_layout);

        this.drawerRV = findViewById(R.id.rv_nav_drawer);
        this.drawerRV.setLayoutManager(new LinearLayoutManager(this));
        this.drawerRV.setHasFixedSize(true);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);

        this.steamPlayerAdapter = new SteamPlayerAdapter(this);
        this.searchResultsRV.setAdapter(steamPlayerAdapter);

        this.steamSearchViewModel = new ViewModelProvider(this)
                .get(SteamSearchViewModel.class);
        this.savedPlayersViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(SavedPlayersViewModel.class);

        this.savedPlayerAdapter = new SavedPlayerAdapter(this);
        this.drawerRV.setAdapter(this.savedPlayerAdapter);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Button videoButton = (Button)findViewById(R.id.btn_video);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               playVideo();
            }
        });


        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchSteamID = searchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchSteamID)) {
                    performSteamIDSearch(STEAM_API_KEY, searchSteamID);
                }
                else {
                    performSteamIDSearch(STEAM_API_KEY, sharedPreferences.getString(
                            getString(R.string.pref_player_key),
                            getString(R.string.pref_player_title)
                    ));
                }
            }
        });

        this.savedPlayersViewModel.getAllSavedPlayers().observe(this, new Observer<List<SavedPlayer>>() {
            @Override
            public void onChanged(List<SavedPlayer> savedPlayers) {
                savedPlayerAdapter.updateSavedPlayersData(savedPlayers);
            }
        });

        RecyclerView drawerRV = findViewById(R.id.rv_nav_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        this.steamSearchViewModel.getPlayerSearchResults().observe(
                this,
                new Observer<PlayerData>() {
                    @Override
                    public void onChanged(PlayerData playerData) {
                        if (playerData != null) {
                            steamPlayerAdapter.updateSearchResults(playerData);
                            playerSummary = playerData.getPlayerSummary();
                            myPersonaStateInt = playerSummary.getPersonastate();
                        }

                    }
                }
        );

        this.steamSearchViewModel.getLoadingStatus().observe(
                this,
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicatorPB.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {

                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);


                            // use this for RecyclerView
                            // forecastListRV.setVisibility(View.VISIBLE);
                        } else {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.VISIBLE);

                            // use this for RecyclerView
                            // forecastListRV.setVisibility(View.INVISIBLE);
                        }
                    }
                }
        );
    }

    public void playVideo(){
        Intent videoPlayerActivityIntent = new Intent(this, VideoPlayerActivity.class);
        startActivity(videoPlayerActivityIntent);
    }


    @Override
    public void onPlayerClick(PlayerSummary playerSummary){
        if( this.playerSummary != null ){
            Intent intent = new Intent(this, FriendsActivity.class);
            intent.putExtra(FriendsActivity.EXTRA_PLAYER_DATA, this.playerSummary);
            startActivity(intent);
        }
    }


    @Override
    public void onSearchResultClicked(SavedPlayer savedPlayer) {
        SharedPreferences.Editor sharedPreferencesEditor = this.sharedPreferences.edit();
        sharedPreferencesEditor.putString(getString(R.string.pref_player_key), savedPlayer.id);
        sharedPreferencesEditor.apply();
        performSteamIDSearch(STEAM_API_KEY, savedPlayer.id);
        this.drawerLayout.closeDrawers();
    }

    private void performSteamIDSearch(String API_KEY, String playerID){
        Log.d(TAG, "== user entered this steamID: " + playerID);
        steamSearchViewModel.loadPlayerSearchResults(API_KEY, playerID);
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(getString(R.string.pref_player_key), playerID);
        editor.commit();

    }
    public void mToast(){
        if (this.searchToast != null) {
            this.searchToast.cancel();
        }
        this.searchToast = Toast.makeText(
                this,
                getString(R.string.search_success),
                Toast.LENGTH_LONG
        );
        this.searchToast.show();
    }
//key: 76561197960435530
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_news:
                getNews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy(){
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_search:
                return true;
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return false;
        }
    }

//key 76561197960435530


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String plid = this.sharedPreferences.getString(getString(R.string.pref_player_key), "0");
        performSteamIDSearch(STEAM_API_KEY, plid);
        long ltime = System.currentTimeMillis();
        boolean isValidString = false;
        SavedPlayer savedPlayer = new SavedPlayer(plid, ltime);

        if (savedPlayer.id == null || savedPlayer.id.length() != 17)
        {
            searchResultsRV.setVisibility(View.INVISIBLE);
            Log.d(TAG,   "Unable to add to database");

        }
        else {
            for (int i = 0; i < 17; i++)
            {
                if (savedPlayer.id.charAt(i) <= '0' || savedPlayer.id.charAt(i) >= '9')
                {
                    searchResultsRV.setVisibility(View.INVISIBLE);
                    Log.d(TAG,   "Unable to add to database");
                    break;
                }
         else{

           }
                isValidString = true;

            }
            if (isValidString == true) {
                searchResultsRV.setVisibility(View.VISIBLE);
                errorMessageTV.setVisibility(View.INVISIBLE);
                savedPlayersViewModel.insertPlayer(savedPlayer);
                mToast();
            }
        }
    }

    public void getNews(){
        Intent newsIntent = new Intent(Intent.ACTION_VIEW);
        newsIntent.setData(Uri.parse("https://store.steampowered.com/explore/new/"));
        startActivity(newsIntent);
    }
}