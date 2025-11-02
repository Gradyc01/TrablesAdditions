package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Sequence.StartGame;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates.LCL_TraverseToFloatingIsland;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.GameLauncher;
import me.depickcator.trablesAdditions.Interfaces.GameSequences;

import java.util.List;

public class StartGameSequence extends GameLauncher {
    private final LostCityLiberationRealm realm;
    private final RealmController controller;
    public StartGameSequence(LostCityLiberationRealm realm, RealmController controller) {
        this.realm = realm;
        this.controller = controller;
    }
    @Override
    protected List<GameSequences> initSequence() {
        return List.of(
                new StartingText(controller),
                new OpenGateway(controller)
        );
    }

    @Override
    protected boolean canStart() {
        return true;
    }

    @Override
    protected void end() {
        realm.setRealmState(new LCL_TraverseToFloatingIsland(realm));
    }
}
