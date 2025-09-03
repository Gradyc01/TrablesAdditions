package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Sequences.StartBoss;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates.WitherRealmState;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates.Wither_BossState;
import me.depickcator.trablesAdditions.Interfaces.GameLauncher;
import me.depickcator.trablesAdditions.Interfaces.GameSequences;

import java.util.List;

public class StartBoss extends GameLauncher {
    private final RealmController controller;
    private final Realm realm;
    private final Wither_BossState startState;
    public StartBoss(RealmController controller, Realm realm, Wither_BossState startState) {
        this.controller = controller;
        this.realm = realm;
        this.startState = startState;
    }
    @Override
    protected List<GameSequences> initSequence() {
        return List.of(
                new TeleportText(controller),
                new TeleportToBossFight("spawn_boss", controller),
                new BossIntroText(controller)
        );
    }

    @Override
    protected boolean canStart() {
        realm.setRealmState(startState);
        return true;
    }

    @Override
    protected void end() {
        startState.setNextBossState();
//        realm.setRealmState(finishedState);
    }
}
