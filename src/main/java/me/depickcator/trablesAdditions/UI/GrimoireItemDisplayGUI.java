package me.depickcator.trablesAdditions.UI;

import me.depickcator.trablesAdditions.Game.Items.CraftData;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrimoireItemDisplayGUI extends TrablesPlayerMenuGUI {
    private final List<CustomItem> items;
    private final Map<ItemStack, Craft> map;
    public GrimoireItemDisplayGUI(PlayerData playerData, List<CustomItem> customItems) {
        super(playerData, 6, TextUtil.makeText("Grimoire", TextUtil.AQUA), true);
        this.items = customItems;
        this.map = new HashMap<>();
        initItems();
    }

    private void initItems() {
        List<CustomItem> items = new ArrayList<>(this.items);
        for (int i = 9; i < 45; i++) {
            if (i % 9 == 0 || i % 9 == 8) continue;
            if (items.isEmpty()) break;
            inventory.setItem(i, initItem(items.getFirst()));
            items.removeFirst();
        }
    }

    private ItemStack initItem(CustomItem item) {
        ItemStack itemStack = item.getResult();
        if (item instanceof Craft craft) {
            ItemMeta meta = itemStack.getItemMeta();
            List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();
            lore.add(TextUtil.makeText("Click to View Recipe", TextUtil.YELLOW));
            meta.lore(lore);
            itemStack.setItemMeta(meta);
            map.put(itemStack, craft);
        }
        return itemStack;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return false;
        Craft craft = map.get(item);
        if (craft == null) return false;
        new ViewCraftGUI(playerData, craft, this);


        return false;
    }
}
