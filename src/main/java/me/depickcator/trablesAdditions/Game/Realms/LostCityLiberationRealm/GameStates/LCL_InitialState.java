package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.List;

public class LCL_InitialState extends LostCityLibState {
    public LCL_InitialState(LostCityLiberationRealm realm) {
        super(realm);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {event.setCancelled(true);}
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {event.setCancelled(true);}
    @Override
    public void onEntityDeath(EntityDeathEvent event) {event.setCancelled(true);}
    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setYield(0);
        event.setCancelled(true);}
    @Override
    public void onBlockExplode(BlockExplodeEvent event) {
//        TextUtil.debugText(event.getBlock().getType().name());
        if (event.getBlock().getType() == Material.AIR) return;
        event.setCancelled(true);
        event.setYield(0);
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, RealmController controller) {
        event.setCancelled(true);
    }
    @Override
    public boolean shouldProgressTime() {
        return false;
    }
    @Override
    public void onPlayerBucket(PlayerBucketEvent event) {event.setCancelled(true);}

    @Override
    public List<Component> getObjectiveName() {
        return List.of(TextUtil.makeText(" Enter Realm", TextUtil.YELLOW));
    }

    @Override
    public String getStateName() {
        return "Initial";
    }
}
