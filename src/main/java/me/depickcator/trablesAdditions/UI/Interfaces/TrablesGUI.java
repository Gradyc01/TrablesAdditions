package me.depickcator.trablesAdditions.UI.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class TrablesGUI {
    protected final int GUISize;
    protected final Inventory inventory;
    protected final TrablesAdditions plugin;
    private static final Map<UUID, Pair<Inventory, TrablesGUI>> guiMap = new HashMap<>();

    /* Creates a GUI for playerData that is GUI lines tall */
    public TrablesGUI(PlayerData playerData, int GUILines, Component name) {
        plugin = TrablesAdditions.getInstance();
        GUISize = GUILines * 9;
        inventory = Bukkit.createInventory(playerData.getPlayer(), GUISize, name);
    }

    /*Opens the GUI for Player player*/
    public void open(Player p) {
        p.openInventory(inventory);
        TrablesGUI.registerGUI(p, inventory, this);
    }

    /* Checks whether a player is holding item
     * Returns true if Yes
     * False Otherwise */
    protected boolean isHolding(Player p, ItemStack item) {
        ItemStack held = p.getInventory().getItemInMainHand();
        return ItemComparison.equalItems(held, item);
    }

    /* Triggers when a player interacts with an item in the GUI */
    public abstract boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event);

    /* Triggers when a player closes the GUI */
    public boolean runWhenCloseGUI(PlayerData playerData, InventoryCloseEvent event) {
        return true; //Stub on purpose
    }

    public static void registerGUI(Player player, Inventory inventory, TrablesGUI gui) {
        guiMap.put(player.getUniqueId(), new MutablePair<>(inventory, gui));
    }
    public static Pair<Inventory, TrablesGUI> findInventory(Player player) {
        return guiMap.get(player.getUniqueId());
    }
    public static void removeGUI(Player player) {
        guiMap.remove(player.getUniqueId());
    }
}
