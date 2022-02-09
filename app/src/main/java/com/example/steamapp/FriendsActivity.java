package com.example.steamapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.steamapp.data.FriendsList;
import com.example.steamapp.data.LoadingStatus;
import com.example.steamapp.data.PlayerSummary;

public class FriendsActivity extends AppCompatActivity {

    public static final String EXTRA_PLAYER_DATA = "FriendsActivity.PlayerSummary";
    public static String CURRENT_PLAYER_STEAM_ID;

    private static final String STEAM_API_KEY = BuildConfig.STEAM_API_KEY;
    private static final String TAG = FriendsActivity.class.getSimpleName();

    private RecyclerView searchResultsRV;

    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;

    private SteamFriendAdapter steamFriendAdapter;

    private PlayerSummary playerSummary = null;
    private TextView friendName;
    private TextView friendStatus;
    private ImageView friendAvatar;
    private String pState;

    // lifecycle stuff
    private FriendSearchViewModel friendSearchViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.searchResultsRV = findViewById(R.id.rv_search_results);

        this.searchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        this.searchResultsRV.setHasFixedSize(true);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);

        this.steamFriendAdapter = new SteamFriendAdapter();
        this.searchResultsRV.setAdapter(steamFriendAdapter);

        this.friendName = findViewById(R.id.tv_player_name);
        this.friendStatus = findViewById(R.id.tv_player_status);
        this.friendAvatar = findViewById(R.id.iv_player_avatar);

        this.friendSearchViewModel = new ViewModelProvider(this)
                .get(FriendSearchViewModel.class);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_PLAYER_DATA)) {
            this.playerSummary = (PlayerSummary)intent.getSerializableExtra(EXTRA_PLAYER_DATA);
        }

        CURRENT_PLAYER_STEAM_ID = this.playerSummary.getSteamid();
        friendSearchViewModel.loadFriendSearchResults(STEAM_API_KEY, this.playerSummary.getSteamid());

        this.friendName.setText(this.playerSummary.getPersonaname());

        // Display status as string value equivalent
        String status;
        switch (this.playerSummary.getPersonastate()) {
            case 0:
                status = "Offline";
                break;
            case 1:
                status = "Online";
                break;
            case 2:
                status = "Busy";
                break;
            case 3:
                status = "Away";
                break;
            case 4:
                status = "Snooze";
                break;
            case 5:
                status = "Looking to Trade";
                break;
            case 6:
                status = "Looking to Play";
                break;
            default:
                status = "Thinking";
        }
        this.friendStatus.setText(status);

        // Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
        Glide.with(this)
                .load(this.playerSummary.getAvatar())
                .into(this.friendAvatar);

        this.friendSearchViewModel.getFriendSearchResults().observe(
                this,
                new Observer<FriendsList>() {
                    @Override
                    public void onChanged(FriendsList friendsList) {
                        steamFriendAdapter.updateSearchResults(friendsList);
                    }
                }
        );

        this.friendSearchViewModel.getLoadingStatus().observe(
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_games:
                Intent intent = new Intent(this, GamesActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_share:
                shareStatus();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareStatus(){
        int myPersonaStateInt = this.playerSummary.getPersonastate();

        switch (myPersonaStateInt) {
            case 0:
                this.pState = "Offline";
                break;
            case 1:
                this.pState = "Online";
                break;
            case 2:
                this.pState = "Busy";
                break;
            case 3:
                this.pState = "Away";
                break;
            case 4:
                this.pState = "Snooze";
                break;
            case 5:
                this.pState = "Looking to Trade";
                break;
            case 6:
               this.pState = "Looking to Play";
                break;
            default:
                this.pState = "Thinking";
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        String shareText = "Check out " + this.playerSummary.getPersonaname() +"'s status on steam!: " + pState;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");
        Intent chooserIntent = Intent.createChooser(shareIntent, null);
        startActivity(chooserIntent);

    }

}
