package com.example.steamapp.data;

import com.google.gson.annotations.SerializedName;

public class PlayerData {
    @SerializedName("response")
    public PlayerSummary playerSummary;

    // should we have these here? maybe... I'm still unsure...
    // ownedGames
    // friends

    public PlayerData(){
        this.playerSummary = new PlayerSummary();
    }

    public PlayerSummary getPlayerSummary() { return playerSummary; }
}
