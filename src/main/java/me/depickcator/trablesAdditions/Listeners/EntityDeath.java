package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerStats;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityDeath extends TrablesListeners {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        String worldName = event.getEntity().getWorld().getName();
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onEntityDeath(event);
        if (event.getDamageSource().getCausingEntity() instanceof Player player) {
            PlayerStats playerStats = PlayerUtil.getPlayerData(player).getPlayerStats();
            playerStats.addNumberStat(PlayerStats.STAT_KILLS, 1);
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        RealmController controller = RealmController.getController(event.getEntity().getWorld().getName());
        if (controller != null) controller.getRealmState().onPlayerDeath(event, controller);

        PlayerStats playerStats = PlayerUtil.getPlayerData(event.getPlayer()).getPlayerStats();
        playerStats.addNumberStat(PlayerStats.STAT_DEATHS, 1);
    }
}
