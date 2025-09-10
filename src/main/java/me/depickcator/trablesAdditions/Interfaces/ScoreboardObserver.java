package me.depickcator.trablesAdditions.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import org.bukkit.scoreboard.Objective;

public interface ScoreboardObserver {
    void update(BoardMaker maker, Objective board, PlayerData playerData);
    String observerName();
}
