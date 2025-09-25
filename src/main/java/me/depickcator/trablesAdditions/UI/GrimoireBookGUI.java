package me.depickcator.trablesAdditions.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

public class GrimoireBookGUI extends TrablesPlayerMenuGUI {
    public GrimoireBookGUI(PlayerData playerData) {
        super(playerData, 6, TextUtil.makeText("Grimoire Book", TextUtil.YELLOW), true);
        initMenu();
    }

    private void initMenu() {
        inventory.setItem(21, makeIcon(Material.WITHER_SKELETON_SKULL,
                TextUtil.makeText("Wither Realm Grimoire", TextUtil.AQUA),
                "wither_realm",
                List.of()));
        inventory.setItem(23, makeIcon(Material.GRASS_BLOCK,
                TextUtil.makeText("Global Grimoire", TextUtil.AQUA),
                "global",
                List.of()));
    }

    private ItemStack makeIcon(Material material, Component title, String tag, List<Component> lore) {
        ItemStack item = initExplainerItem(material, lore, title);
        ItemMeta meta = item.getItemMeta();
        CustomModelDataComponent component = meta.getCustomModelDataComponent();
        component.setStrings(List.of(tag));
        meta.setCustomModelDataComponent(component);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return false;
        if (!item.getItemMeta().hasCustomModelDataComponent()) return false;
        String tag = item.getItemMeta().getCustomModelDataComponent().getStrings().getFirst();
        switch (tag) {
            case "wither_realm" -> {
                new GrimoireItemDisplayGUI(playerData, plugin.getCraftData().getWitherRealmItems());
            }
            case "global" -> {
                new GrimoireItemDisplayGUI(playerData, plugin.getCraftData().getGlobalItems());
            }
            default -> {/*Do Nothing*/}
        }
        return false;
    }
}
