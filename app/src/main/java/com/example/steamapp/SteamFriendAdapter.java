package com.example.steamapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steamapp.data.FriendInfo;
import com.example.steamapp.data.FriendsList;

public class SteamFriendAdapter extends RecyclerView.Adapter<SteamFriendAdapter.SearchResultViewHolder>{
    private FriendsList friendsList;
    private static final String TAG = SteamFriendAdapter.class.getSimpleName();

    public void updateSearchResults(FriendsList searchResultsList) {
        this.friendsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.friendsList == null || this.friendsList.getFriends() == null) {
            return 0;
        } else {
            return this.friendsList.getFriends().getFriendInfo().size();
        }
    }

    @NonNull
    @Override
    public SteamFriendAdapter.SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friend_list, parent, false);
        return new SteamFriendAdapter.SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SteamFriendAdapter.SearchResultViewHolder holder, int position) {
        holder.bind(this.friendsList.getFriends().getFriendInfo().get(position));
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView searchResultTV;

        SearchResultViewHolder(View itemView) {
            super(itemView);
            this.searchResultTV = itemView.findViewById(R.id.tv_friend_id_new);
        }

        void bind(FriendInfo searchResult) {
            Log.d(TAG, "data from bind fx: " + searchResult.getSteamid());
            Context ctx = this.itemView.getContext();

            // Display persona name
            // this.searchResultTV.setTextColor(Color.parseColor("#ffffff"));
            this.searchResultTV.setText(searchResult.getSteamid());
        }
    }
}