package me.depickcator.trablesAdditions.Interfaces;

import me.depickcator.trablesAdditions.TrablesAdditions;

public abstract class GameSequences {
    protected final TrablesAdditions plugin;
    private final String sequenceName;
    public GameSequences(String sequenceName) {
        this.sequenceName = sequenceName;
        plugin = TrablesAdditions.getInstance();
    }
    /*Runs a certain sequence of events that help start the game
     * Calls the callback at the end*/
    public abstract void run(GameLauncher game);

    public String getSequenceName() {
        return sequenceName;
    }
}
