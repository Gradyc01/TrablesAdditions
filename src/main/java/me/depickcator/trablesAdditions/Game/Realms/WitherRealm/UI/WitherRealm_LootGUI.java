package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealmLoot;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Lidded;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Random;

public class WitherRealm_LootGUI extends TrablesBlockGUI {
    private boolean hasBeenFound;
    private final WitherRealm realm;
    public WitherRealm_LootGUI(Block block, WitherRealmLoot loot, Random random, WitherRealm realm) {
        super(block, 6, TextUtil.makeText(loot.getTierName() + " Chest", TextUtil.AQUA));
        loot.populateLoot(inventory, random, 1.0);
        this.realm = realm;
        hasBeenFound = false;
    }

    @Override
    public boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event) {
        if (event.getAction().isRightClick() && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            open(playerData.getPlayer());
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        }

        return false;
    }

    @Override
    public void open(Player p) {
        super.open(p);
        hasBeenFound =true;
        if (getBlock().getState() instanceof Lidded lidded) lidded.open();
        new PlayerArmSwingEvent(p, EquipmentSlot.HAND);
        if (realm != null) realm.updateLootedCount(PlayerUtil.getPlayerData(p));
    }

    @Override
    public boolean runWhenCloseGUI(PlayerData playerData, InventoryCloseEvent event) {
        getBlock().getWorld().playSound(getBlock().getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
        return true;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        return true;
    }

    public boolean isHasBeenFound() {
        return hasBeenFound;
    }
}
