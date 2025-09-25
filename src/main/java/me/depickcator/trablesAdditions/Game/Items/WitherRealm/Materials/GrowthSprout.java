package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemDrop;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemReforge;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.N;

import java.util.List;

public class GrowthSprout extends CustomItem implements ItemDrop, ItemReforge, ItemClick {
    private static GrowthSprout instance;
//    private final AttributeModifier modifier;
    private GrowthSprout() {
        this("Growth Sprout", "growth_sprout");
    }

    protected GrowthSprout(String displayName, String key) {
        super(displayName, key);
        registerItem(this, this);
        registerClick(this, this);
        registerReforge(this);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.OAK_SAPLING);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.DARK_GREEN).append(TextUtil.applyText()));
        meta.lore(List.of(
                TextUtil.makeText("A new wave of green has flourished into the", TextUtil.DARK_PURPLE),
                TextUtil.makeText("realm increasing the health of the individual", TextUtil.DARK_PURPLE)));
        meta.setMaxStackSize(1);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    public static GrowthSprout getInstance() {
        if (instance == null) {
            instance = new GrowthSprout();
        }
        return instance;
    }

    @Override
    public boolean uponApply(InventoryClickEvent e, ItemStack appliedOn, ItemStack applying, PlayerData pD) {
        if (!ItemUtil.canBeWorn(appliedOn)) return false;
        ItemReforge oldReforge = ItemReforge.getReforge(appliedOn);
        if (oldReforge != null) {
            if (oldReforge instanceof GrowthSprout) return false;
            oldReforge.removeReforge(appliedOn);
        }
        addReforge(appliedOn);
        applying.setAmount(applying.getAmount() - 1);
        ItemReforge.addReforgeTag(appliedOn, this);
        pD.getPlayer().playSound(pD.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 2);
        return false;
    }

    @Override
    public void addReforge(ItemStack item) {
        if (ItemUtil.canBeWorn(item)) {
            ItemMeta meta = item.getItemMeta();
            if (meta.getAttributeModifiers(Attribute.ARMOR) == null) {
                ItemUtil.addArmorAttributes(item);
                meta = item.getItemMeta();
            }
            meta.addAttributeModifier(Attribute.MAX_HEALTH, createModifier(item));
            item.setItemMeta(meta);
        }
    }

    @Override
    public void removeReforge(ItemStack item) {
        if (ItemUtil.canBeWorn(item)) {
            ItemMeta meta = item.getItemMeta();
            meta.removeAttributeModifier(Attribute.MAX_HEALTH, createModifier(item));
            item.setItemMeta(meta);
        }
    }

    protected AttributeModifier createModifier(ItemStack item) {
        NamespacedKey key = new NamespacedKey(TrablesAdditions.getInstance(), getKey() + ItemComparison.itemParser(item));
        return new AttributeModifier(key, 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR);
    }

    @Override
    public String getReforgeName() {
        return "GROWTH";
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        return false;
    }
}
