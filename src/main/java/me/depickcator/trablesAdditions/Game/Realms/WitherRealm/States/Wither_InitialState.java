package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.States;

import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class Wither_InitialState extends WitherRealmState{

    public Wither_InitialState(WitherRealm realm) {
        super(realm);
    }

    @Override
    public void onMobSpawn(CreatureSpawnEvent event) {/*event.setCancelled(true);*/}
    @Override
    public void onBlockBreak(BlockBreakEvent event) {event.setCancelled(true);}
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {event.setCancelled(true);}
    @Override
    public void onEntityDeath(EntityDeathEvent event) {event.setCancelled(true);}
    @Override
    public void onEntityExplode(EntityExplodeEvent event) {event.setCancelled(true);}

    @Override
    public String getStateName() {
        return "Initial";
    }
}
