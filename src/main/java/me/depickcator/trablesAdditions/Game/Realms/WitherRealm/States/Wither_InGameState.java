package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.States;

import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;

public class Wither_InGameState extends WitherRealmState{

    public Wither_InGameState(WitherRealm realm) {
        super(realm);
    }

    @Override
    public String getStateName() {
        return "In Game";
    }
}
