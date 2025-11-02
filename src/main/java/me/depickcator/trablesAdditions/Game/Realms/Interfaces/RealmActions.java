package me.depickcator.trablesAdditions.Game.Realms.Interfaces;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;

public abstract class RealmActions {
    protected RealmController controller;
    protected String meshName;
    public RealmActions(String meshName, RealmController controller) {
        this.meshName = meshName;
        this.controller = controller;
    }
    public abstract boolean start();

    public String getMeshName() {
        return meshName;
    }
}
