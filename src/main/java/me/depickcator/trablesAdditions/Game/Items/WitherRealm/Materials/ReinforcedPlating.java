package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemDrop;
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

public class ReinforcedPlating extends CustomItem implements ItemDrop {
    private static ReinforcedPlating instance;
    private ReinforcedPlating() {
        super("Reinforced Plating", "reinforced_plating");
        registerItem(this, this);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.GRAY).append(TextUtil.applyText()));
        meta.lore(List.of(
                TextUtil.makeText("This plating weaves enchanted alloys, ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("increasing the durability of the item", TextUtil.DARK_PURPLE)));
        item.setItemMeta(meta);
        meta.setMaxStackSize(1);
        meta.setEnchantmentGlintOverride(true);
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
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
        if (appliedOn.getType().equals(Material.SHIELD)) {
            applying.setAmount(applying.getAmount() - 1);
            pD.getPlayer().playSound(pD.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 0);
            Damageable meta = (Damageable) appliedOn.getItemMeta();
            meta.setMaxDamage(500);
            if (meta.displayName() != null) {
                meta.displayName(meta.displayName().append(TextUtil.makeText(" [Reinforced]", TextUtil.WHITE)));
            }
            appliedOn.setItemMeta(meta);
            return true;
        }
        return false;
    }
}
