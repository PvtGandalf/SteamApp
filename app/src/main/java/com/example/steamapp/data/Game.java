package com.example.steamapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Game {

    @SerializedName("game_count")
    public int gameCount;

    @SerializedName("games")
    public ArrayList<GameInfo> gameInfo;

    public Game(){
        this.gameInfo = null;
        this.gameCount = 0;
    }

    public ArrayList<GameInfo> getGameInfo() { return this.gameInfo; }

    public int getGameCount() { return this.gameCount; }
}
