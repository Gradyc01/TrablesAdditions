package me.depickcator.trablesAdditions;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerStats;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.Scoreboards.DefaultBoard;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;
import java.util.Map;

public class WorldStats implements ScoreboardObserver {
    private final TrablesAdditions plugin;
    private final Map<String, Integer> numberStats;
    public WorldStats() {
        this.plugin = TrablesAdditions.getInstance();
        this.numberStats = new HashMap<>();
        DefaultBoard.getInstance().addObserver(this);
    }

    public void setNumberStat(String statName, int number) {
        numberStats.put(statName, number);
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

    public void updateStats() {
        setNumberStat("players", Bukkit.getOnlinePlayers().size());
        for (PlayerData pD : PlayerUtil.getAllPlayerData()) {
            updateStats(pD);
            pD.getPlayerScoreboards().updateBoard(this);
        }
    }

    public void updateStats(PlayerData playerData) {
        PlayerStats playerStats = playerData.getPlayerStats();
        Player player = playerData.getPlayer();
        playerStats.setNumberStat(PlayerStats.STAT_DISTANCE_TRAVELED, player.getStatistic(Statistic.WALK_ONE_CM));
        playerStats.setNumberStat(PlayerStats.STAT_TOTAL_WORLD_TIME, player.getStatistic(Statistic.TOTAL_WORLD_TIME));
    }

    @Override
    public void update(BoardMaker maker, Objective board, PlayerData playerData) {
        maker.editLine(board, 13, TextUtil.makeText(" Players Online: " + getNumberStat("players")));
    }

    @Override
    public String observerName() {
        return "WorldStats";
    }
}
