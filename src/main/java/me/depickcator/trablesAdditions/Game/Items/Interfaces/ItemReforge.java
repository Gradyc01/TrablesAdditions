package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ItemReforge {
    Map<String, ItemReforge> items = new HashMap<>();
    NamespacedKey key = new NamespacedKey(TrablesAdditions.getInstance(), "ItemReforge");
    NamespacedKey lineKey = new NamespacedKey(TrablesAdditions.getInstance(), "ItemReforge_Line");

    void addReforge(ItemStack item);
    void removeReforge(ItemStack item);
    String getReforgeName();

    default void registerReforge(ItemReforge itemReforge) {
        items.put(getReforgeName(), itemReforge);
    }

    static ItemReforge getReforge(ItemStack item) {
        if (item == null) return null;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(key, PersistentDataType.STRING)) return items.get(container.get(key, PersistentDataType.STRING));
        return null;
    }

    static void addReforgeTag(ItemStack item, ItemReforge itemReforge) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<Component> lore = meta.lore() == null ? new ArrayList<>() : meta.lore();

        if (container.has(lineKey, PersistentDataType.INTEGER)) {
            int line = container.get(lineKey, PersistentDataType.INTEGER);
            lore.set(line, TextUtil.makeText(itemReforge.getReforgeName().toUpperCase(), TextUtil.WHITE, true, false));
        } else {
            int line = lore.size();
            container.set(lineKey, PersistentDataType.INTEGER, line);
            lore.add(line, TextUtil.makeText(itemReforge.getReforgeName().toUpperCase(), TextUtil.WHITE, true, false));
        }
        meta.lore(lore);
        container.set(key, PersistentDataType.STRING, itemReforge.getReforgeName());
        item.setItemMeta(meta);
    }
}
