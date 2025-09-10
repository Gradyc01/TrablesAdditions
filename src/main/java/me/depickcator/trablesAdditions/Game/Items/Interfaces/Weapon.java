package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Weapon extends Craft {
    private final double attackDamage;
    private final double attackSpeed;
    private static final Map<String, Weapon> customWeapons = new HashMap<>();
    protected Weapon(String displayName, String key, double attackDamage, double attackSpeed, boolean removeVanillaRecipe) {
        super(displayName, key, removeVanillaRecipe);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        addCustomWeaponsTag(result);
        addModifiers(result);
    }
    protected Weapon(String displayName, String key, double attackDamage, double attackSpeed) {
        this(displayName, key, attackDamage, attackSpeed, false);
    }

    private void addCustomWeaponsTag(ItemStack item) {
//        ItemMeta meta = item.getItemMeta();
//        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, getKey());
//        item.setItemMeta(meta);
        customWeapons.put(ItemComparison.itemParser(item), this);
    }

    /*Adds Sword Effects*/
    protected ItemStack addSwordEffects(ItemStack item) {
//        item.setData(DataComponentTypes.BLOCKS_ATTACKS, BlocksAttacks.blocksAttacks().blockDelaySeconds(0));
//        item.setData(DataComponentTypes.BLOCKS_ATTACKS, BlocksAttacks.blocksAttacks().blockSound(Key.key("entity.player.attack.nodamage")));
        return item;
    }

    public static Weapon findCustomWeapon(ItemStack item) {
        return customWeapons.get(ItemComparison.itemParser(item));
    }

    public abstract boolean onCustomWeaponUse(EntityDamageByEntityEvent event, LivingEntity attacker, LivingEntity target);

    protected void addModifiers(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.removeAttributeModifier(Attribute.ATTACK_SPEED);
        meta.removeAttributeModifier(Attribute.ATTACK_DAMAGE);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, makeAttackDamageModifier(getKey(), attackDamage));
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, makeAttackSpeedModifier(getKey(), attackSpeed));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.lore(makeFakeLore(attackDamage, attackSpeed, meta.lore()));
        item.setItemMeta(meta);
    }
    protected AttributeModifier makeAttackSpeedModifier(String key, double num) {
        return new AttributeModifier(
                NamespacedKey.minecraft(key),
                num,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.MAINHAND);
    }
    protected AttributeModifier makeAttackDamageModifier(String key, double num) {
        return  new AttributeModifier(
                NamespacedKey.minecraft(key),
                num - 1 ,// Inorder to calculate must add a -1 to compensate for the 1 base damage
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.MAINHAND);
    }

    protected List<Component> makeFakeLore(double attackDamage, double attackSpeed, List<Component> oldLore) {
        List<Component> lore = oldLore == null ? new ArrayList<>() : new ArrayList<>(oldLore);
        lore.add(TextUtil.makeText("", TextUtil.GRAY));
        lore.add(TextUtil.makeText("When in Main Hand:", TextUtil.GRAY));
        lore.add(TextUtil.makeText(" " + Math.round(attackDamage * 10) / 10.0 + " Attack Damage", TextUtil.DARK_GREEN));
        lore.add(TextUtil.makeText(" " + Math.round((4 + attackSpeed) * 10) / 10.0 + " Attack Speed", TextUtil.DARK_GREEN));
//        int critDamage = KEY.contains("axe") ? 200 : 150;
//        lore.add(TextUtil.makeText(" " + critDamage + "% Crit Damage", TextUtil.DARK_GREEN));
        return lore;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }
}
