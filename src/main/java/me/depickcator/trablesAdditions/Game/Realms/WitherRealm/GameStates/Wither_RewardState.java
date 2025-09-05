package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEvent;

import java.util.List;

public class Wither_RewardState extends WitherRealmState{

    public Wither_RewardState(WitherRealm realm) {
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
    public List<Component> getObjectiveName() {
        return List.of(TextUtil.makeText(" Collect Rewards", TextUtil.YELLOW));
    }

    @Override
    public void onPlayerBucket(PlayerBucketEvent event) {event.setCancelled(true);}

    @Override
    public String getStateName() {
        return "Reward";
    }
}
