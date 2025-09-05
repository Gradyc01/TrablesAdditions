package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeave extends TrablesListeners {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PlayerUtil.assignNewPlayerData(event.getPlayer());
        String worldName = event.getPlayer().getWorld().getName();
        TextUtil.debugText("World Name: " + worldName);
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onPlayerJoin(event, controller);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        String worldName = event.getPlayer().getWorld().getName();
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onPlayerLeave(event, controller);

        PlayerUtil.removePlayerData(event.getPlayer());
    }
}
