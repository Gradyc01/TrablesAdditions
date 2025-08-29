package me.depickcator.trablesAdditions.Game.Items.Uncraftable;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemDrop;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RepairKit extends CustomItem implements ItemDrop {
    private static RepairKit instance;
    private RepairKit() {
        super("Repair Kit", "repair_kit");
        registerItem(this, this);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.SHEARS);
        ItemMeta meta = item.getItemMeta();
        meta.setEnchantmentGlintOverride(true);
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.WHITE));
        meta.lore(List.of(
                TextUtil.makeText("Apply this to any item to fully repair it", TextUtil.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    @Override
    public boolean uponApply(InventoryClickEvent e, ItemStack appliedOn, ItemStack applying, PlayerData pD) {
        if (appliedOn.getItemMeta() instanceof Damageable damageable) {
            if (damageable.getDamage() > 0) {
                damageable.setDamage(0);
                appliedOn.setItemMeta(damageable);
                applying.setAmount(0);
                pD.getPlayer().playSound(pD.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 0);
                return true;
            }
        }
        return false;
    }

    public static RepairKit getInstance() {
        if (instance == null) {
            instance = new RepairKit();
        }
        return instance;
    }

}
