package me.depickcator.trablesAdditions.Game.Realms.WitherRealm;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class WitherRealm extends Realm {

    public WitherRealm(Location location) {
        super(location, "test-world-1");

    }

    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        Player player = playerData.getPlayer();
        Location loc = player.getLocation();
        TextUtil.broadcastMessage(TextUtil.makeText(player.getName() + " has activated WitherRealm it will be placed at"
        + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ(), TextUtil.AQUA));
        if (!loc.getBlock().isLiquid()) {
//            openPortal();
            new RealmController(this).start();
        } else return false;
        return true;
    }

    @Override
    public void openPortal() {

    }

    @Override
    public void closePortal() {

    }
}
