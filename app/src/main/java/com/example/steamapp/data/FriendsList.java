package com.example.steamapp.data;

import com.google.gson.annotations.SerializedName;

public class FriendsList {
    @SerializedName("friendslist")
    public Friend friends;

    public FriendsList(){
        this.friends = null;
    }

    public Friend getFriends() { return friends; }
}