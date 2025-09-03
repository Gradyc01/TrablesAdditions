package me.depickcator.trablesAdditions.UI.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public abstract class TrablesPlayerMenuGUI extends TrablesMenuGUI {
    protected final Player player;
    protected final PlayerData playerData;
    /* Creates a GUI for playerData that is GUI lines tall */
    public TrablesPlayerMenuGUI(PlayerData playerData, int GUILines, Component name, boolean setBackground, boolean openInstantly) {
        super(playerData.getPlayer(), GUILines, name, setBackground);
        this.playerData = playerData;
        this.player = playerData.getPlayer();
        if (openInstantly) open(player);
    }
    public TrablesPlayerMenuGUI(PlayerData playerData, int GUILines, Component name, boolean setBackground) {
        this(playerData, GUILines, name, setBackground, true);
    }

    /* Checks whether a player is holding item
     * Returns true if Yes
     * False Otherwise */
    protected boolean isHolding(ItemStack item) {
//        ItemStack held = player.getInventory().getItemInMainHand();
//        return equalItems(held, item);
        return isHolding(player, item);
    }

    /* The standardized Player Head button */
    protected void playerHeadButton(int index) {
        ItemStack button = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) button.getItemMeta();
        headMeta.setPlayerProfile(player.getPlayerProfile());
        Component title = Component.text("Your Stats").color(TextColor.color(TextUtil.GOLD));
        title = title.decoration(TextDecoration.ITALIC, false);
        headMeta.displayName(title);
        button.setItemMeta(headMeta);
        inventory.setItem(index, button);
    }

    /* Triggers when a player interacts with an item in the GUI */
    public abstract boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event);
}
