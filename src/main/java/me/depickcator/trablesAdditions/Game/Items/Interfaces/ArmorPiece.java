package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerArmorEffects;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.ArrayList;
import java.util.List;

public abstract class ArmorPiece extends CustomItem {
    private final ArmorSet armorSet;
    public ArmorPiece(String displayName, String key, ArmorSet armorSet, int armorSlot) {
        super(displayName, key);
        this.armorSet = armorSet;
        if (this.armorSet != null) armorSet.setSlot(armorSlot, this);
        plugin.getCraftData().registerArmorPiece(this);
    }

    public ArmorPiece(String displayName, String key) {
        this(displayName, key, null, -1);
    }

    public abstract void triggerTemporaryDamageArmorEffects(PlayerArmorEffects effects, EntityDamageByEntityEvent event);
    protected abstract void addStatArmorEffects(PlayerArmorEffects effects);
    protected abstract void removeStatArmorEffects(PlayerArmorEffects effects);

    public void addStatArmor(PlayerArmorEffects effects) {
        addStatArmorEffects(effects);
        checkFullArmorSetBonus(effects);
    }

    public void removeStatArmor(PlayerArmorEffects effects) {
        removeStatArmorEffects(effects);
        checkFullArmorSetBonus(effects);
    }

    private void checkFullArmorSetBonus(PlayerArmorEffects effects) {
        if (armorSet != null) armorSet.checkForFullArmorSetBonus(effects);
    }

    protected ItemStack makeArmor(Material material, int durability, double armor, double toughness,
                                  EquipmentSlotGroup equipmentSlotGroup, Component displayName, List<Component> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);

        meta.addAttributeModifier(Attribute.ARMOR,
                new AttributeModifier(new NamespacedKey(TrablesAdditions.getInstance(), getKey() + "_armor"),
                        armor, AttributeModifier.Operation.ADD_NUMBER, equipmentSlotGroup));
        meta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS,
                new AttributeModifier(new NamespacedKey(TrablesAdditions.getInstance(), getKey() + "_armor_toughness"),
                        toughness, AttributeModifier.Operation.ADD_NUMBER, equipmentSlotGroup));
        String slot = equipmentSlotGroup.toString();
        slot = slot.substring(0, 1).toUpperCase() + slot.substring(1);
        List<Component> newLore = new ArrayList<>(lore);
        newLore.add(TextUtil.makeText(""));
        newLore.add(TextUtil.makeText("When on " + slot + ":", TextUtil.GRAY));
        newLore.add(TextUtil.makeText(" " + Math.round(armor * 10) / 10.0 + " Armor", TextUtil.BLUE));
        newLore.add(TextUtil.makeText(" " + Math.round((toughness) * 10) / 10.0 + " Armor Toughness", TextUtil.BLUE));
        meta.lore(newLore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        if (item.getItemMeta() instanceof Damageable damageable) {
            damageable.setMaxDamage(durability);
            item.setItemMeta(damageable);
        }
        generateUniqueModelString(item);
        return item;
    }

    protected void addArmorTrim(ItemStack item, TrimMaterial material, TrimPattern pattern) {
        if (item.getItemMeta() instanceof ArmorMeta meta) {
//            ArmorMeta meta = (ArmorMeta) item.getItemMeta();
            meta.setTrim(new ArmorTrim(material, pattern));
            item.setItemMeta(meta);
        }

    }

    public ArmorSet getArmorSet() {
        return armorSet;
    }
}
