package me.depickcator.trablesAdditions.Game.Realms.WitherRealm;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class WitherRealm extends Realm {

    public WitherRealm(Location location) {
        super(location, "TestRealm", "Test Realm");
    }

    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        Player player = playerData.getPlayer();
        Location loc = player.getLocation();
        TextUtil.broadcastMessage(TextUtil.makeText(player.getName() + " has activated WitherRealm it will be placed at"
        + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ(), TextUtil.AQUA));
        if (!loc.getBlock().isLiquid()) {
            new RealmController(this).initialize();
        } else return false;
        return true;
    }

    @Override
    public void onMobSpawn(CreatureSpawnEvent event) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!block.hasMetadata("PLACED")) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        block.setMetadata("PLACED", new FixedMetadataValue(TrablesAdditions.getInstance(), true));
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {

    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> blocks = event.blockList();
        for (Block block : new ArrayList<>(blocks)) {
            if (!block.hasMetadata("PLACED")) {
                blocks.remove(block);
            }
        }
    }
}
