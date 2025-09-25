package me.depickcator.trablesAdditions.Persistence;

import com.google.gson.JsonObject;

public interface PlayerReadable {
    /*Warning method will most likely be run asynchronously */
    void readJson(JsonObject jsonObject);
}
