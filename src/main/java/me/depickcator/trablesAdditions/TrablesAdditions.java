package me.depickcator.trablesAdditions;

import me.depickcator.Test.Commands.*;
import me.depickcator.trablesAdditions.Game.Items.CraftData;
import me.depickcator.trablesAdditions.Game.Mechanics.EntityInteractions;
import me.depickcator.trablesAdditions.Listeners.*;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kick(TextUtil.makeText("Server Restarting", TextUtil.DARK_RED));
        }
//        this.getServer().getScheduler().runTaskLater(this, () -> {TextUtil.debugText("Stopping Server now Bye");}, 60 * 20);
//        PlayerUtil.clearPlayerDataMap();
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
        new EntityDamage(); new PlayerJoinLeave(); new ProjectileLaunch();
        new EquipmentChange(); new PlayerCraftingEvent(); new ServerOpenClose();
    }
}
