package me.depickcator.trablesAdditions.UI;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class ViewCraftGUI extends ViewRecipeGUI {
    private final TrablesPlayerMenuGUI gui;
    public ViewCraftGUI(PlayerData playerData, Craft craft, TrablesPlayerMenuGUI gui) {
        super(playerData, 6, TextUtil.makeText("Viewing Craft:" + craft.getDisplayName(), TextUtil.AQUA),
                craft.getRecipe(), craft.getResult());
        this.gui = gui;
        inventory.setItem(48, goBackItem());
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return false;
        if (item.equals(goBackItem())) {
            gui.open(playerData.getPlayer());
        }
        return false;
    }
}
