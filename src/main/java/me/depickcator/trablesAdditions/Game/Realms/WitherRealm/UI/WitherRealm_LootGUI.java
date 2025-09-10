package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealmLoot;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class WitherRealm_LootGUI extends TrablesBlockGUI {
    public WitherRealm_LootGUI(Block block, WitherRealmLoot loot, Random random) {
        super(block, 6, TextUtil.makeText(loot.getTierName() + " Chest", TextUtil.AQUA));
        loot.populateLoot(inventory, random, 1.0);
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
    public boolean runWhenCloseGUI(PlayerData playerData, InventoryCloseEvent event) {
        getBlock().getWorld().playSound(getBlock().getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
        return true;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        return true;
    }
}
