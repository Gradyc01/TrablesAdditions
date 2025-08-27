package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.States;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public abstract class WitherRealmState implements RealmStates {
    private final WitherRealm realm;
    public WitherRealmState(WitherRealm realm) {
        this.realm = realm;
    }

    @Override
    public void onPlayerBucket(PlayerBucketEvent event) {
        ItemStack bucketResult = event.getItemStack();
        Block targetBlock = event.getBlock();
        if (bucketResult != null && bucketResult.getType().equals(Material.BUCKET)) {
            if (event.getBucket().equals(Material.WATER_BUCKET)) targetBlock = treatWaterLogging(event);
            if (targetBlock != null) {
                realm.addPlacedBlock(targetBlock);
            }
        } else {
            if (realm.containsPlacedBlock(targetBlock)) {
                realm.removeBlock(targetBlock);
            }
        }
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
        if (block.getType().equals(Material.TNT)) {
            Block placedBlock = event.getBlockPlaced();
            placedBlock.setType(Material.AIR);
            placedBlock.getWorld().spawnEntity(placedBlock.getLocation().clone().add(0.5, 0 , 0.5), EntityType.TNT);
            return;
        }
        realm.addPlacedBlock(block);
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

    private Block treatWaterLogging(PlayerBucketEvent event) {
        ItemStack bucketResult = event.getItemStack();
        Block targetBlock = event.getBlock();
        if (targetBlock.getBlockData() instanceof Waterlogged waterlogged) {
            Block relative = event.getBlockClicked().getRelative(event.getBlockFace());
            if (relative.getType() != Material.AIR) {
                int num = event.getPlayer().getEyeHeight() >= targetBlock.getY() ? 1 : -1;
                relative = targetBlock.getLocation().clone().add(0, num,0).getBlock();
            }
            if (relative.getType() == Material.AIR) {
                relative.setType(Material.WATER);
            } else {
                event.setCancelled(true);
                return null;
            }
            event.setCancelled(true);
            if (event.getHand() == EquipmentSlot.HAND) event.getPlayer().getInventory().setItemInMainHand(bucketResult);
            else event.getPlayer().getInventory().setItemInOffHand(bucketResult);
            return relative;
        }
        return targetBlock;
    }
}
