package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Sequences.GameOver;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.GameLauncher;
import me.depickcator.trablesAdditions.Interfaces.GameSequences;

import java.util.List;

public class GameOver extends GameLauncher {
    private final RealmController controller;
    public GameOver(RealmController controller) {
        this.controller = controller;
    }
    @Override
    protected List<GameSequences> initSequence() {
        return List.of(new EndGameText(controller));
    }

    @Override
    protected boolean canStart() {
        return true;
    }

    @Override
    protected void end() {
        controller.stopRealm();
    }
}
