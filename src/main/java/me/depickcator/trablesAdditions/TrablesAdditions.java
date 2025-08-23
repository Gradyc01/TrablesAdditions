package me.depickcator.trablesAdditions;

import me.depickcator.Test.Commands.CreateWorld;
import me.depickcator.Test.Commands.Debugger;
import me.depickcator.Test.Commands.TrablesTest;
import me.depickcator.Test.Commands.Travel;
import me.depickcator.trablesAdditions.Game.Mechanics.EntityInteractions;
import me.depickcator.trablesAdditions.Listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class TrablesAdditions extends JavaPlugin {
    private static TrablesAdditions instance;
    private EntityInteractions entityInteractions;
    private TrablesAdditions() {

    }

    @Override
    public void onEnable() {
        instance = this;
        initCommands();
        initListeners();
        entityInteractions = new EntityInteractions();
    }

    @Override
    public void onDisable() {

    }

    public EntityInteractions getEntityInteractions() {return entityInteractions;}

    public static TrablesAdditions getInstance() {
        return instance;
    }

    private void initCommands() {
        new Debugger(); new TrablesTest(); new CreateWorld(); new Travel();
    }

    private void initListeners() {
        new InventoryListener(); new DimensionalTravel(); new BlockChange();
        new EntityDeath(); new MobSpawning(); new PlayerInteractListener();
    }
}
