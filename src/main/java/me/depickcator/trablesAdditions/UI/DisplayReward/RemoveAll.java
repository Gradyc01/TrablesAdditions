package me.depickcator.trablesAdditions.UI.DisplayReward;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RemoveAll implements TrablesMenuActionable {
    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        return false;
    }
}
