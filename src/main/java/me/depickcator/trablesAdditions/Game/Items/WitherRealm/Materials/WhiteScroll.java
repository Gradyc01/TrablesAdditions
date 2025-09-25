package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemDrop;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemReforge;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class WhiteScroll extends CustomItem implements ItemDrop, ItemReforge {
    private static WhiteScroll instance;
    public static NamespacedKey key = new NamespacedKey(TrablesAdditions.getInstance(), "WhiteScrolled");
    private WhiteScroll() {
        super("White Scroll", "white_scroll");
        registerItem(this, this);
        registerReforge(this);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.GRAY).append(TextUtil.applyText()));
        meta.lore(List.of(
                TextUtil.makeText("Protects this item from being lost on death", TextUtil.DARK_PURPLE)));
        meta.setMaxStackSize(1);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    public static WhiteScroll getInstance() {
        if (instance == null) {
            instance = new WhiteScroll();
        }
        return instance;
    }

    @Override
    public boolean uponApply(InventoryClickEvent e, ItemStack appliedOn, ItemStack applying, PlayerData pD) {
        if (appliedOn.getItemMeta() instanceof Damageable meta) {
            ItemReforge oldReforge = ItemReforge.getReforge(appliedOn);
            if (oldReforge != null) {
                if (oldReforge instanceof WhiteScroll) {
                    return false;
                }
                oldReforge.removeReforge(appliedOn);
            }
            applying.setAmount(applying.getAmount() - 1);
            addReforge(appliedOn);
            ItemReforge.addReforgeTag(appliedOn, this);
            pD.getPlayer().playSound(pD.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 0);
        }
        return false;
    }

    @Override
    public void addReforge(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(WhiteScroll.key, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
    }

    @Override
    public void removeReforge(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(WhiteScroll.key, PersistentDataType.BOOLEAN)) {
            container.remove(WhiteScroll.key);
        }
        item.setItemMeta(meta);
    }

    @Override
    public String getReforgeName() {
        return "white scrolled";
    }
}
