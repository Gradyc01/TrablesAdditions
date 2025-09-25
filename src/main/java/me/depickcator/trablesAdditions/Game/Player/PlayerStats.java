package me.depickcator.trablesAdditions.Game.Player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.Persistence.PlayerReadable;
import me.depickcator.trablesAdditions.Persistence.PlayerWritable;
import me.depickcator.trablesAdditions.Scoreboards.DefaultBoard;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PlayerStats implements ScoreboardObserver, PlayerWritable, PlayerReadable {
    public static String STAT_KILLS = "kills";
    public static String STAT_DEATHS = "deaths";
    private final TrablesAdditions plugin;
    private final Player player;
    private final Map<String, Integer> numberStats;
    private final PlayerData playerData;
    public PlayerStats(PlayerData playerData) {
        this.playerData = playerData;
        this.player = playerData.getPlayer();
        this.plugin = TrablesAdditions.getInstance();
        this.numberStats = new HashMap<>();
        DefaultBoard.getInstance().addObserver(this);
    }

    public void setNumberStat(String statName, int number) {
        numberStats.put(statName, number);
        playerData.getPlayerScoreboards().updateBoard(this);
    }

    public void addNumberStat(String statName, int number) {
        setNumberStat(statName, getNumberStat(statName) + number);

    }

    public int getNumberStat(String statName) {
        if (!numberStats.containsKey(statName)) {
            numberStats.put(statName, 0);
        }
        return numberStats.get(statName);
    }

    @Override
    public void update(BoardMaker maker, Objective board, PlayerData playerData) {
        maker.editLine(board, 3, TextUtil.makeText(" Kills: " + TextUtil.formatNumber(getNumberStat(STAT_KILLS))));
        maker.editLine(board, 2, TextUtil.makeText(" Deaths: " + TextUtil.formatNumber(getNumberStat(STAT_DEATHS))));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty(STAT_KILLS, getNumberStat(STAT_KILLS));
        json.addProperty(STAT_DEATHS, getNumberStat(STAT_DEATHS));
        return json;
    }

    @Override
    public void readJson(JsonObject jsonObject) {
        if (jsonObject.has("stats")) {
            JsonObject object = jsonObject.get("stats").getAsJsonObject();
            setNumberStat(STAT_KILLS, object.get(STAT_KILLS).getAsInt());
            setNumberStat(STAT_DEATHS, object.get(STAT_DEATHS).getAsInt());
        }
    }

    public void delete() {
        DefaultBoard.getInstance().removeObserver(this);
    }

    @Override
    public String observerName() {
        return "PlayerStats: " + player.getName();
    }


}
