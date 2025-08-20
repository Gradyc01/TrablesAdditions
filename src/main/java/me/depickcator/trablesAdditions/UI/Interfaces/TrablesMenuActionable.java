package me.depickcator.trablesAdditions.UI.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface TrablesMenuActionable {
    boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event);
}
