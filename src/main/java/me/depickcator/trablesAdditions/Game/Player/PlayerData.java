package me.depickcator.trablesAdditions.Game.Player;

import org.bukkit.entity.Player;

public class PlayerData {
    private final Player player;
    private final PlayerScoreboards playerScoreboards;
    private final PlayerStats playerStats;
    public PlayerData(Player player) {
        this.player = player;
        playerStats = new PlayerStats(this);
        playerScoreboards = new PlayerScoreboards(this);

    }

    public Player getPlayer() {
        return player;
    }

    public PlayerScoreboards getPlayerScoreboards() {
        return playerScoreboards;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public void saveData() {
        playerStats.delete();
    }
}
