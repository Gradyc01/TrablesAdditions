package me.depickcator.trablesAdditions;

import me.depickcator.Test.Commands.*;
import me.depickcator.trablesAdditions.Game.Items.CraftData;
import me.depickcator.trablesAdditions.Game.Mechanics.EntityInteractions;
import me.depickcator.trablesAdditions.Listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class TrablesAdditions extends JavaPlugin {
    private static TrablesAdditions instance;
    private EntityInteractions entityInteractions;
    private CraftData craftData;
    private TrablesAdditions() {

    }

    @Override
    public void onEnable() {
        instance = this;
        initCommands();
        initListeners();
        entityInteractions = new EntityInteractions();
        craftData = new CraftData();
        craftData.initCrafts();
    }

    @Override
    public void onDisable() {

    }

    public EntityInteractions getEntityInteractions() {return entityInteractions;}

    public static TrablesAdditions getInstance() {
        return instance;
    }

    public CraftData getCraftData() {
        return craftData;
    }

    private void initCommands() {
        new Debugger(); new TrablesTest(); new CreateWorld(); new Travel(); new GiveCustomItem();
    }

    private void initListeners() {
        new InventoryListener(); new DimensionalTravel(); new BlockChange();
        new EntityDeath(); new MobSpawning(); new PlayerInteractListener();
        new onDamage(); new PlayerJoinLeave();
    }
}
