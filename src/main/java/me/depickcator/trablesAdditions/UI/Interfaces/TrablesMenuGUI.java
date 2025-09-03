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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public abstract class TrablesMenuGUI extends TrablesGUI {
    /* Creates a GUI for playerData that is GUI lines tall */
    public TrablesMenuGUI(InventoryHolder holder, int GUILines, Component name, boolean setBackground) {
        super(holder, GUILines, name);
        if (setBackground) setBackground();
    }

    protected void setBackground() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText(""));
        item.setItemMeta(meta);
        for (int i = 0; i < GUISize; i++) {
            inventory.setItem(i, item);
        }
    }

    /* The standardized close button */
    protected ItemStack getCloseButton() {
        ItemStack button = new ItemStack(Material.BARRIER);
        ItemMeta buttonMeta = button.getItemMeta();
        buttonMeta.displayName(TextUtil.makeText("Close", TextUtil.DARK_RED));
        buttonMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        button.setItemMeta(buttonMeta);
        return button;
    }

    /* The standardized go back button */
    protected ItemStack goBackItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText("Go Back", TextUtil.DARK_GRAY));
        item.setItemMeta(meta);
        return item;
    }

    /* The standardized net page button */
    protected ItemStack nextPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText("Next Page", TextUtil.DARK_GRAY));
        item.setItemMeta(meta);
        return item;
    }

    /* The standardized previous page button */
    protected ItemStack previousPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText("Previous Page", TextUtil.DARK_GRAY));
        item.setItemMeta(meta);
        return item;
    }



    /* The standardized Explainer item */
    protected ItemStack initExplainerItem(Material material, List<Component> lore, Component name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        AttributeModifier buttonModifier = new AttributeModifier(NamespacedKey.minecraft("hide_main_menu"),
                2, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, buttonModifier);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(name);
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /* Triggers when a player interacts with an item in the GUI */
    public abstract boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event);
}
