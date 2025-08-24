package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.States;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public abstract class WitherRealmState implements RealmStates {
    private final WitherRealm realm;
    public WitherRealmState(WitherRealm realm) {
        this.realm = realm;
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
        if (block.getType().equals(Material.TNT)) {
            Block placedBlock = event.getBlockPlaced();
            placedBlock.setType(Material.AIR);
            placedBlock.getWorld().spawnEntity(placedBlock.getLocation().clone().add(0.5, 0 , 0.5), EntityType.TNT);
        }
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {

    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        onBlockExploded(event.blockList());
    }

    @Override
    public void onBlockExplode(BlockExplodeEvent event) {
        onBlockExploded(event.blockList());
    }

    private void onBlockExploded(List<Block> blockList) {
        for (Block block : new ArrayList<>(blockList)) {
            if (!block.hasMetadata("PLACED")) {
                blockList.remove(block);
            }
        }
    }

    public WitherRealm getRealm() {
        return realm;
    }
}
