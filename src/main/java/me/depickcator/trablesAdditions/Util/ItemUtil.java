package me.depickcator.trablesAdditions.Util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.depickcator.trablesAdditions.TrablesAdditions;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ItemUtil {
    public static ItemStack buildHead(String base64, Component name, List<Component> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        moldHead(item, base64);
//        SkullMeta meta = (SkullMeta) item.getItemMeta();
//        PlayerProfile profile = Bukkit.createProfile(UUID.fromString("5f856526-a7c6-4782-bcf9-803e02b08e1d"), null);
//        profile.getProperties().add(new ProfileProperty("textures", base64));
//        meta.setPlayerProfile(profile);
//        meta.displayName(name);
//        meta.lore(lore);
//        item.setItemMeta(meta);
        Repairable meta1 = (Repairable) item.getItemMeta();
        meta1.setRepairCost(999);
        meta1.displayName(name);
        meta1.lore(lore);
        item.setItemMeta(meta1);
        return item;
    }

    public static ItemStack moldHead(ItemStack item, String base64) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.fromString("5f856526-a7c6-4782-bcf9-803e02b08e1d"), null);
        profile.getProperties().add(new ProfileProperty("textures", base64));
        meta.setPlayerProfile(profile);
        item.setItemMeta(meta);
        return item;
    }

    /*Returns true if not on cooldown and sets the cooldown, False otherwise*/
    public static boolean checkCooldown(Player p, ItemStack item, float seconds) {
        if (!p.hasCooldown(item)) {
            p.setCooldown(item, (int) (seconds * 20));
            return true;
        }
        return false;
    }

    public static boolean canBeWorn(ItemStack item) {
        return (item.getItemMeta() instanceof ArmorMeta) || TrablesAdditions.getInstance().getCraftData().findArmorPiece(item) != null;
    }

    public static Pair<Integer, Float> getArmorValues(Material material) {
        return switch (material) {
            case LEATHER_HELMET, GOLDEN_BOOTS, CHAINMAIL_BOOTS, LEATHER_BOOTS -> Pair.of(1, 0.0f);
            case LEATHER_CHESTPLATE, GOLDEN_LEGGINGS -> Pair.of(3, 0.0f);
            case LEATHER_LEGGINGS, GOLDEN_HELMET, IRON_BOOTS, IRON_HELMET, CHAINMAIL_HELMET, TURTLE_HELMET -> Pair.of(2, 0.0f);

            case CHAINMAIL_CHESTPLATE, GOLDEN_CHESTPLATE, IRON_LEGGINGS -> Pair.of(5, 0.0f);
            case CHAINMAIL_LEGGINGS -> Pair.of(4, 0.0f);

            case IRON_CHESTPLATE -> Pair.of(6, 0.0f);

            case DIAMOND_HELMET, DIAMOND_BOOTS -> Pair.of(3, 2.0f);
            case DIAMOND_CHESTPLATE -> Pair.of(8, 2.0f);
            case DIAMOND_LEGGINGS -> Pair.of(6, 2.0f);

            case NETHERITE_HELMET, NETHERITE_BOOTS -> Pair.of(3, 3.0f);
            case NETHERITE_CHESTPLATE -> Pair.of(8, 3.0f);
            case NETHERITE_LEGGINGS -> Pair.of(6, 3.0f);

            default -> Pair.of(0, 0.0f); // Not armor
        };
    }

    public static void addArmorAttributes(ItemStack item) {
        TextUtil.debugText("DDD");
        Pair<Integer, Float> armorValues = getArmorValues(item.getType());
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(TrablesAdditions.getInstance(), ItemComparison.itemParser(item));
        meta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(key, (double) armorValues.getLeft(),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR));
        meta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(key, (double) armorValues.getRight(),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR));
        item.setItemMeta(meta);
    }
}
