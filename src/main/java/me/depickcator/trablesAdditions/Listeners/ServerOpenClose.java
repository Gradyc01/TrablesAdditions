package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.PlayerDataWriter;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class ServerOpenClose extends TrablesListeners {

    @EventHandler
    public void onServerClose(PluginDisableEvent event) {
        TextUtil.debugText(event.getPlugin().getName() + "ADSLDASKJDAKLSJDKLAJDLKASJDKLJKLDJLSAKLDsAJKLDSKLA");
//        PlayerUtil.clearPlayerDataMap();
        for (PlayerData pD : PlayerUtil.getAllPlayerData()) {
            pD.getPlayerInventories().saveInventory();
            new PlayerDataWriter(pD).write();
//            pD.saveData();
        }
    }
}
