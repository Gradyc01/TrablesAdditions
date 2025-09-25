package me.depickcator.trablesAdditions.Persistence;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.Player;

import java.io.IOException;

public class PlayerDataWriter {
    private final PlayerData playerData;
    private final Player player;
    private final JsonObject json;
    private final String destination;
    public PlayerDataWriter(PlayerData playerData) {
        this.playerData = playerData;
        this.player = playerData.getPlayer();
        this.json = new JsonObject();
        this.destination = "./plugins/TrablesAdditions/PlayerData/" + this.player.getUniqueId() + ".json";
    }

    private void writePlayerData() {
        buildJson("stats", playerData.getPlayerStats());
        buildJson("inventories", playerData.getPlayerInventories());
    }

    private void buildJson(String name, PlayerWritable writable) {
        json.add(name, writable.toJson());

    }
    public void write() {
        writePlayerData();
        JsonWriter jsonWriter = new JsonWriter(destination);
        try {
            jsonWriter.open();
            jsonWriter.write(json);
            jsonWriter.close();
            TextUtil.debugText("PlayerData Writer " + player.getName(), " Finished Writing PlayerData");
        } catch (IOException e) {
            TextUtil.debugText("Failed to write stats to file");
        }
    }
}
