package me.depickcator.trablesAdditions.Scoreboards;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.scoreboard.Objective;

public class DefaultBoard extends BoardMaker {
    private static DefaultBoard board;
    private DefaultBoard() {
        super("Default");
    }

    @Override
    public void makeBoard(Objective board, PlayerData playerData) {
        blankBoard(board, 0, 14);
        for (ScoreboardObserver observer : getObservers()) {
            observer.update(this, board, playerData);
        }
//        editLine(board, 14, TextUtil.makeText("AyeDu stinks", TextUtil.YELLOW));
    }

    public static DefaultBoard getInstance() {
        if (board == null) board = new DefaultBoard();
        return board;
    }

}
