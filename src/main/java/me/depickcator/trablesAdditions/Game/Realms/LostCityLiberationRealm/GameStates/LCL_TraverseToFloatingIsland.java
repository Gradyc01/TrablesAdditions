package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Actions.TeleportToSpawns;
import me.depickcator.trablesAdditions.Persistence.RealmMeshReader;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class LCL_TraverseToFloatingIsland extends LostCityLibState {
    private boolean setNewState;
    public LCL_TraverseToFloatingIsland(LostCityLiberationRealm realm) {
        super(realm);
        setNewState = false;
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
    public void onPlayerBucket(PlayerBucketEvent event) {
        event.setCancelled(true);
    }

    @Override
    public boolean onDimensionalTravel(PlayerPortalEvent event, RealmController controller) {
//        if (controller.getWorld().equals(event.getFrom().getWorld())) {
            Player player = event.getPlayer();
            if (!new TeleportToSpawns("portal_overworld_exit", controller, player).start()) controller.stopRealm();
            if (!setNewState) {
                setNewState = true;
                TextUtil.debugText("Starting LCL E1");
                getRealm().setRealmState(new LCL_E1_Start(getRealm(), controller));
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 5 * 20, 0, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 0, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 5 * 20, 0, true, false));
//        }
        return true;
    }


    @Override
    public List<Component> getObjectiveName() {
        return List.of(TextUtil.makeText(" Enter Realm", TextUtil.YELLOW));
    }

    @Override
    public String getStateName() {
        return "Traverse To Floating Island";
    }
}
