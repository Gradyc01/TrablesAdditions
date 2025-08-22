package me.depickcator.trablesAdditions;

import me.depickcator.Test.Commands.CreateWorld;
import me.depickcator.Test.Commands.Debugger;
import me.depickcator.Test.Commands.TrablesTest;
import me.depickcator.Test.Commands.Travel;
import me.depickcator.trablesAdditions.Listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class TrablesAdditions extends JavaPlugin {
    private static TrablesAdditions instance;
    private TrablesAdditions() {

    }

    @Override
    public void onEnable() {
        instance = this;
        initCommands();
        initListeners();
    }

    @Override
    public void onDisable() {

    }

    public static TrablesAdditions getInstance() {
        return instance;
    }

    private void initCommands() {
        new Debugger(); new TrablesTest(); new CreateWorld(); new Travel();
    }

    private void initListeners() {
        new InventoryListener(); new DimensionalTravel(); new BlockChange();
        new EntityDeath(); new MobSpawning();
    }
}
