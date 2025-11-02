package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.PlayerDataWriter;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeave extends TrablesListeners {
    private final TrablesAdditions plugin;

    public PlayerJoinLeave() {
        super();
        plugin = TrablesAdditions.getInstance();
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        plugin.getWorldStats().updateStats();
        PlayerUtil.assignNewPlayerData(event.getPlayer());
        String worldName = event.getPlayer().getWorld().getName();
//        for (Attribute attribute : Attribute) {
//            entity.getAttribute(attribute).getModifiers().clear(); // Remove all modifiers for this attribute
//        }
        TextUtil.debugText("World Name: " + worldName);
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onPlayerJoin(event, controller);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        TextUtil.debugText("Player Quit");
        plugin.getWorldStats().updateStats();
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onPlayerLeave(event, controller);
//        PlayerData pD = PlayerUtil.getPlayerData(player);

        PlayerUtil.removePlayerData(player);
    }
}
