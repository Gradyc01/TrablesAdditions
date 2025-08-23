package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.States;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
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
