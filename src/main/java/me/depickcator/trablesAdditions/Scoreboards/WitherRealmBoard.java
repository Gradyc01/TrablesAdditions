package me.depickcator.trablesAdditions.Scoreboards;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import org.bukkit.scoreboard.Objective;

public class WitherRealmBoard extends BoardMaker {
    private static WitherRealmBoard board;
    private WitherRealmBoard() {
        super("Wither Realm");
    }

    @Override
    public void makeBoard(Objective board, PlayerData playerData) {
        blankBoard(board, 0, 14);
        for (ScoreboardObserver observer : getObservers()) {
            observer.update(this, board, playerData);
        }
    }

    public static WitherRealmBoard getInstance() {
        if (board == null) board = new WitherRealmBoard();
        return board;
    }

}
