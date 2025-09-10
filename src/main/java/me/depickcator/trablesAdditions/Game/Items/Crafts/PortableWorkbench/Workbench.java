package me.depickcator.trablesAdditions.Game.Items.Crafts.PortableWorkbench;

import me.depickcator.trablesAdditions.TrablesAdditions;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Workbench {
    private final NamespacedKey key;
    private final MenuType type;
    private final Material material;
    private final String name;
    public Workbench(MenuType type, Material material, String name) {
        this.key = new NamespacedKey(TrablesAdditions.getInstance(), material.name().toLowerCase() + "_workbench");
        this.type = type;
        this.material = material;
        this.name = name;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public MenuType getType() {
        return type;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public void setKeyOnItem(ItemStack item, boolean value) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(getKey(), PersistentDataType.BOOLEAN, value);
        item.setItemMeta(meta);
    }

    public Pair<Workbench, Boolean> getKeyOnItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(getKey(), PersistentDataType.BOOLEAN)) {
            return Pair.of(this, container.get(getKey(), PersistentDataType.BOOLEAN));
        } else {
            container.set(getKey(), PersistentDataType.BOOLEAN, false);
            return Pair.of(this, false);
        }
    }
}
