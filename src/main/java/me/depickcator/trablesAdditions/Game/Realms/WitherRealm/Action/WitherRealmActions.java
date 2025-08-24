package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;

public abstract class WitherRealmActions {
    protected RealmController controller;
    protected String meshName;
    public WitherRealmActions(String meshName, RealmController controller) {
        this.meshName = meshName;
        this.controller = controller;
    }
    public abstract boolean start();
}
