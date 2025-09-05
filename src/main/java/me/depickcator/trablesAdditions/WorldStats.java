package me.depickcator.trablesAdditions;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.Scoreboards.DefaultBoard;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;
import java.util.Map;

public class WorldStats implements ScoreboardObserver {
    private final TrablesAdditions plugin;
    private final Map<String, Integer> numberStats;
    public WorldStats(World world) {
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

    @Override
    public void update(BoardMaker maker, Objective board, PlayerData playerData) {

    }
}
