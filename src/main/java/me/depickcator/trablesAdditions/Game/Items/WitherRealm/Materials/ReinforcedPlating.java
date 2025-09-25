package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemDrop;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemReforge;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ReinforcedPlating extends CustomItem implements ItemDrop, ItemReforge {
    private static ReinforcedPlating instance;
    private ReinforcedPlating() {
        super("Reinforced Plating", "reinforced_plating");
        registerItem(this, this);
        registerReforge(this);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.GRAY).append(TextUtil.applyText()));
        meta.lore(List.of(
                TextUtil.makeText("This plating weaves enchanted alloys, ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("increasing the durability of the item", TextUtil.DARK_PURPLE)));
        meta.setMaxStackSize(1);
        meta.setEnchantmentGlintOverride(true);
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    public static ReinforcedPlating getInstance() {
        if (instance == null) {
            instance = new ReinforcedPlating();
        }
        return instance;
    }

    @Override
    public boolean uponApply(InventoryClickEvent e, ItemStack appliedOn, ItemStack applying, PlayerData pD) {
        if (appliedOn.getItemMeta() instanceof Damageable meta) {
            ItemReforge oldReforge = ItemReforge.getReforge(appliedOn);
            if (oldReforge != null) {
                if (oldReforge instanceof ReinforcedPlating) {
                    return false;
                }
                oldReforge.removeReforge(appliedOn);
            }
            addReforge(appliedOn);
            applying.setAmount(applying.getAmount() - 1);
            ItemReforge.addReforgeTag(appliedOn, this);
            pD.getPlayer().playSound(pD.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 0);
        }
        return false;
    }

    @Override
    public void addReforge(ItemStack item) {
        if (item.getItemMeta() instanceof Damageable meta) {
            int maxDamage = meta.hasMaxDamage() ? meta.getMaxDamage() : item.getType().getMaxDurability();
            meta.setMaxDamage((int) (maxDamage * 1.5));
            item.setItemMeta(meta);
        }
    }

    @Override
    public void removeReforge(ItemStack item) {
        if (item.getItemMeta() instanceof Damageable meta) {
            meta.setMaxDamage((int) (meta.getMaxDamage() / 1.5));
            item.setItemMeta(meta);
        }
    }

    @Override
    public String getReforgeName() {
        return "Reinforced";
    }
}
