package com.example.steamapp.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class FriendInfo {
    private String steamid;

    // default constructor
    public FriendInfo() {
        this.steamid = null;
    }

    // non-default constructor
    public FriendInfo(String steamid) {
        this.steamid = steamid;
    }

    // getters
    public String getSteamid() { return this.steamid; }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<FriendInfo> {
        @Override
        public FriendInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject friendsListObj = json.getAsJsonObject();
            JsonObject friendsObj = friendsListObj.getAsJsonObject();

            return new FriendInfo(
                    friendsObj.getAsJsonPrimitive("steamid").getAsString()
            );
        }
    }
}
