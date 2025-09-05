package me.depickcator.trablesAdditions.Game.Player;

import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealmBossFight;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.Scoreboards.DefaultBoard;
import me.depickcator.trablesAdditions.Scoreboards.WitherRealmBoard;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats implements ScoreboardObserver {
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
        WitherRealmBoard.getInstance().addObserver(this);
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
        maker.editLine(board, 3, TextUtil.makeText(" Kills: " + getNumberStat(STAT_KILLS)));
        maker.editLine(board, 2, TextUtil.makeText(" Deaths: " + getNumberStat(STAT_DEATHS)));
    }

    public void delete() {
        DefaultBoard.getInstance().removeObserver(this);
        WitherRealmBoard.getInstance().removeObserver(this);
    }


}
