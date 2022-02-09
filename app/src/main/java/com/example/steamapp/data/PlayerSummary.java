package com.example.steamapp.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

public class PlayerSummary implements Serializable {

    private String steamid;
    private String personaname;
    private String avatar;
    private int personastate;

    // default constructor
    public PlayerSummary() {
        this.steamid = null;
        this.personaname = null;
        this.avatar = null;
        this.personastate = -1;
    }

    // non-default constructor
    public PlayerSummary(String steamid, String personaname, String avatar, int personastate) {
        this.steamid = steamid;
        this.personaname = personaname;
        this.avatar = avatar;
        this.personastate = personastate;
    }

    // getters
    public String getSteamid() { return this.steamid; }
    public String getPersonaname() { return this.personaname; }
    public String getAvatar() { return this.avatar; }
    public int getPersonastate() { return this.personastate; }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<PlayerSummary> {
        @Override
        public PlayerSummary deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject responseObj = json.getAsJsonObject();
            JsonArray playersArr = responseObj.getAsJsonArray("players");
            JsonObject playersObj = playersArr.get(0).getAsJsonObject();

            return new PlayerSummary(
                    playersObj.getAsJsonPrimitive("steamid").getAsString(),
                    playersObj.getAsJsonPrimitive("personaname").getAsString(),
                    playersObj.getAsJsonPrimitive("avatar").getAsString(),
                    playersObj.getAsJsonPrimitive("personastate").getAsInt()
            );
        }
    }
}
