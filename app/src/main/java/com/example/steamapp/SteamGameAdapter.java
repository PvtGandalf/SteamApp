package com.example.steamapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.steamapp.data.GameInfo;
import com.example.steamapp.data.GamesList;

public class SteamGameAdapter extends RecyclerView.Adapter<SteamGameAdapter.SearchResultViewHolder>{
    private GamesList gamesList;
    private static final String TAG = SteamGameAdapter.class.getSimpleName();

    public void updateSearchResults(GamesList searchResultsList) {
        this.gamesList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.gamesList == null || this.gamesList.getGames() == null) {
            return 0;
        }
        else if (this.gamesList.getGames().getGameInfo() == null){
            return -1;
        }
        else {
            return this.gamesList.getGames().getGameInfo().size();
        }
    }

    @NonNull
    @Override
    public SteamGameAdapter.SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_game_item, parent, false);
        return new SteamGameAdapter.SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SteamGameAdapter.SearchResultViewHolder holder, int position) {
        holder.bind(this.gamesList.getGames().getGameInfo().get(position));
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView searchResultTV;
        private ImageView gameLogoIV;

        SearchResultViewHolder(View itemView) {
            super(itemView);
            this.searchResultTV = itemView.findViewById(R.id.tv_search_result);
            this.gameLogoIV = itemView.findViewById(R.id.iv_game_logo);
        }

        void bind(GameInfo searchResult) {
            Log.d(TAG, "data from bind fx: " + searchResult.getName());
            Log.d(TAG, "image url: " + searchResult.getImgLogoUrl());
            Context ctx = this.itemView.getContext();

            // Display game name
            this.searchResultTV.setText(searchResult.getName());

            // Display game logo
            Glide.with(ctx)
                    .load(searchResult.getImgLogoUrl())
                    .into(gameLogoIV);
        }
    }
}