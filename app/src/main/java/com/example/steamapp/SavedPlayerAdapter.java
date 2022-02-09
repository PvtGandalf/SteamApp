package com.example.steamapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steamapp.data.SavedPlayer;

import java.util.List;

public class SavedPlayerAdapter extends RecyclerView.Adapter <SavedPlayerAdapter.SavedPlayerViewHolder> {
    private OnSearchResultClickedListener onSearchResultClickedListener;
    private List<SavedPlayer> savedPlayerList;

    interface OnSearchResultClickedListener {
        void onSearchResultClicked(SavedPlayer savedPlayer);
    }

    public SavedPlayerAdapter(OnSearchResultClickedListener onSearchResultClickedListener) {
        this.onSearchResultClickedListener = onSearchResultClickedListener;
    }

    public void updateSavedPlayersData(List<SavedPlayer> savedPlayers) {
        this.savedPlayerList = savedPlayers;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (this.savedPlayerList == null) {
            return 0;
        } else {
            return this.savedPlayerList.size();
        }
    }

    @NonNull
    @Override
    public SavedPlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.saved_player_item, parent, false);
        return new SavedPlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedPlayerViewHolder holder, int position){
        holder.bind(savedPlayerList.get(position));
    }


    class SavedPlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView savedNameTV;
        private TextView savedidTV;

        SavedPlayerViewHolder(View itemView) {
            super(itemView);
           // savedNameTV = itemView.findViewById(R.id.tv_savedplayer_name);
            savedidTV = itemView.findViewById(R.id.tv_savedplayer_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSearchResultClickedListener.onSearchResultClicked(
                            savedPlayerList.get(getAdapterPosition())
                    );
                }
            });
        }

        void bind(SavedPlayer savedPlayer){
            //savedNameTV.setText(savedPlayer.name);
            this.savedidTV.setTextColor(Color.parseColor("#ffffff"));
            this.savedidTV.setText(savedPlayer.id);

        }
    }
}
