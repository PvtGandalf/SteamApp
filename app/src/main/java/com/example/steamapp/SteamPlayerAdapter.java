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
import com.example.steamapp.data.PlayerData;
import com.example.steamapp.data.PlayerSummary;

public class SteamPlayerAdapter extends RecyclerView.Adapter<SteamPlayerAdapter.SearchResultViewHolder>{

    private static final String TAG = SteamPlayerAdapter.class.getSimpleName();

    private PlayerData playerData;
    private OnPlayerClickListener onPlayerClickListener;

    public interface OnPlayerClickListener {
        void onPlayerClick(PlayerSummary playerSummary);
    }

    public SteamPlayerAdapter(OnPlayerClickListener onPlayerClickListener) {
        this.onPlayerClickListener = onPlayerClickListener;
    }

    public void updateSearchResults(PlayerData searchResultsList) {
        this.playerData = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.playerData == null || this.playerData.getPlayerSummary() == null){
            return 0;
        } else {
            return 1; // since we always have just one player in the main activity
        }
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.bind(this.playerData.getPlayerSummary());
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView searchResultTV;
        private ImageView playerAvatarIV;
        private TextView playerStatusTV;

        SearchResultViewHolder(View itemView) {
            super(itemView);
            this.searchResultTV = itemView.findViewById(R.id.tv_search_result);
            this.playerAvatarIV = itemView.findViewById(R.id.iv_player_avatar);
            this.playerStatusTV = itemView.findViewById(R.id.tv_player_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPlayerClickListener.onPlayerClick(
                            playerData.getPlayerSummary()
                    );
                }
            });
        }

        void bind(PlayerSummary searchResult) {
            Log.d(TAG, "data from bind fx: " + searchResult.getPersonaname());
            Context ctx = this.itemView.getContext();

            // Display persona name
            this.searchResultTV.setText(searchResult.getPersonaname());

            // Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
            Glide.with(ctx)
                    .load(playerData.getPlayerSummary().getAvatar())
                    .into(playerAvatarIV);

            // Display status as string value equivalent
            String status;
            switch (searchResult.getPersonastate()) {
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
            this.playerStatusTV.setText("Status: " + status);
        }
    }
}
