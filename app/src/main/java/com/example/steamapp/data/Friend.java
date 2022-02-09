package com.example.steamapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Friend {
    @SerializedName("friends")
    public List<FriendInfo> friendInfo;

    public Friend(){
        this.friendInfo = null;
    }

    public List<FriendInfo> getFriendInfo() { return friendInfo; }
}
