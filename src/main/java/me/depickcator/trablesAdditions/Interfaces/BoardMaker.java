package me.depickcator.trablesAdditions.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class BoardMaker {
    private final String boardName;
    private final Set<ScoreboardObserver> observers;
    private final Set<PlayerData> boardViewers;
    public BoardMaker(String boardName) {
        this.boardName = boardName;
        this.observers = new HashSet<>();
        this.boardViewers = new HashSet<>();
    }

    /*Initializes this board maker for PlayerData playerData */
    public void initialize(Objective board, PlayerData playerData) {
        boardViewers.add(playerData);
        makeBoard(board, playerData);
    }

    /*Removes this board maker for PlayerData playerData */
    public void remove(PlayerData playerData) {
        boardViewers.remove(playerData);
    }

    public void updateAllViewers(ScoreboardObserver observer) {
        for (PlayerData playerData : boardViewers) {
            playerData.getPlayerScoreboards().updateBoard(observer);
        }
    }

    protected abstract void makeBoard(Objective board, PlayerData playerData);

    public void blankBoard(Objective board, int start, int end) {
        for (int i = start; i <= end; i++) {
            Score score = board.getScore("" + i);
            score.setScore(i);
            score.customName(TextUtil.makeText("                 "));
        }
    }

    public void editLine(Objective board, int line, Component text) {
        Score score = board.getScore("" + line);
        score.customName(text);
    }

    public boolean addObserver(ScoreboardObserver observer) {
        if (observers.add(observer)) {
            TextUtil.debugText("Board Observer " + getBoardName(), " added another observer " + observer.observerName() + "     " + observers.size());
            return true;
        }
        return false;
    }

    public boolean containsObserver(ScoreboardObserver observer) {
        return observers.contains(observer);
    }

    public Set<ScoreboardObserver> getObservers() {
        return Collections.unmodifiableSet(observers);
    }


    public boolean removeObserver(ScoreboardObserver observer) {
        if (observers.remove(observer)) {
            TextUtil.debugText("Board Observer", " removed an observer " + observers.size());
            return true;
        };
        return false;
    }

    public String getBoardName() {
        return boardName;
    }
}
