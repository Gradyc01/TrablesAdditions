package me.depickcator.trablesAdditions.UI.LoadoutBuilderUI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class OpenLoadoutBuilder implements TrablesMenuActionable {
    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        new LoadoutBuilderGUI(playerData);
        return true;
    }
}
