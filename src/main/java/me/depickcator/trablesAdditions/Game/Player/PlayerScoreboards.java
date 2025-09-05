package me.depickcator.trablesAdditions.Game.Player;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.Scoreboards.DefaultBoard;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class PlayerScoreboards {
    private final TrablesAdditions plugin;
//    private final Set<ScoreboardObserver> needUpdateObservers;
    private BoardMaker boardMaker;
//    private final Scoreboard scoreboard;
    private final Player player;
    private final PlayerData playerData;
    private final Objective board;
    private final Objective healthTab;
    private final Objective healthBelowName;
    public PlayerScoreboards(PlayerData playerData) {
        this.playerData = playerData;
        this.player = playerData.getPlayer();
        this.plugin = TrablesAdditions.getInstance();
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        board = initBoard(scoreboard);
        healthTab = initHealth(scoreboard, DisplaySlot.PLAYER_LIST);
        healthBelowName = initHealth(scoreboard, DisplaySlot.BELOW_NAME);
        setBoardMaker(DefaultBoard.getInstance());
        player.setScoreboard(scoreboard);
    }

    public void updateBoard(ScoreboardObserver observer) {
        if (boardMaker.containsObserver(observer)) {
            observer.update(getBoard(), board, playerData);
        }
    }

    private Objective initBoard(Scoreboard scoreboard) {
        Objective board = scoreboard.registerNewObjective(
                "board",
                Criteria.DUMMY,
                TextUtil.makeText("Trables", TextUtil.YELLOW, true, false));
        board.numberFormat(NumberFormat.blank());
        board.setDisplaySlot(DisplaySlot.SIDEBAR);
        return board;
    }

    private Objective initHealth(Scoreboard scoreboard, DisplaySlot displaySlot) {
        Objective health = scoreboard.registerNewObjective(
                "health_" + displaySlot.name(),
                Criteria.HEALTH,
                TextUtil.makeText("♡", TextUtil.RED, true, false)
        );
        health.setRenderType(RenderType.INTEGER);
        health.setDisplaySlot(displaySlot);
        return health;
        //❤
    }

    public BoardMaker getBoard() {
        return boardMaker;
    }

    public void setBoardMaker(BoardMaker board) {
        TextUtil.debugText(player.getName() + " Scoreboard", "Changing BoardMaker to " + board.getBoardName());
        if (this.boardMaker != null) this.boardMaker.remove(playerData);
        this.boardMaker = board;
        this.boardMaker.initialize(this.board, playerData);
    }
    }
