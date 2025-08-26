package me.depickcator.trablesAdditions.Game.Realms.Interfaces;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;

public interface RealmStates {
    void onMobSpawn(CreatureSpawnEvent event);
    void onBlockBreak(BlockBreakEvent event);
    void onBlockPlace(BlockPlaceEvent event);
    void onEntityDeath(EntityDeathEvent event);
    void onEntityExplode(EntityExplodeEvent event);
    void onBlockExplode(BlockExplodeEvent event);
    void onPlayerBucket(PlayerBucketEvent event);
    String getStateName();
}
