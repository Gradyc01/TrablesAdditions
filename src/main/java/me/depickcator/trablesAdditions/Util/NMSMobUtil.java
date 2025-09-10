package me.depickcator.trablesAdditions.Util;

import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.ItemDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class NMSMobUtil {
    public static void generateRandomArmor(EntityEquipment equipment, Random r) {
        equipment.set(EquipmentSlot.HEAD,
                CraftItemStack.asNMSCopy(
                        CraftItemStack.asNewCraftStack(
                                Mob.getEquipmentForSlot(EquipmentSlot.HEAD, generateNumber(r)))));
        equipment.set(EquipmentSlot.CHEST,
                CraftItemStack.asNMSCopy(
                        CraftItemStack.asNewCraftStack(
                                Mob.getEquipmentForSlot(EquipmentSlot.CHEST, generateNumber(r)))));
        equipment.set(EquipmentSlot.LEGS,
                CraftItemStack.asNMSCopy(
                        CraftItemStack.asNewCraftStack(
                                Mob.getEquipmentForSlot(EquipmentSlot.LEGS, generateNumber(r)))));
        equipment.set(EquipmentSlot.FEET,
                CraftItemStack.asNMSCopy(
                        CraftItemStack.asNewCraftStack(
                                Mob.getEquipmentForSlot(EquipmentSlot.FEET, generateNumber(r)))));
    }

    private static int generateNumber(Random random) {
        if (random.nextDouble() < 0.75) {
            return -1;
        } else if (random.nextDouble() < 0.85) {
            return 0;
        } else if (random.nextDouble() < 0.93) {
            return 1;
        } else if (random.nextDouble() < 0.98) {
            return 2;
        }
        return 3;
    }

    public static Component generateHealthText(Component name, LivingEntity entity) {
        float health = entity.getHealth();
        float maxHealth = entity.getMaxHealth();
        String healthText = "    " + String.format("%.1f", health) + "/" + Math.round(maxHealth) + "❤";
        return name.copy().append(Component.literal(healthText).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.AQUA.value()))));
    }

    public static void attemptToDropItemStack(ItemStack item, DamageSource damageSource, Entity entity, double dropChance) {
        AttributeInstance attribute = null;
        if (damageSource.getEntity() instanceof LivingEntity livingEntity) attribute = livingEntity.getAttribute(Attributes.LUCK);
        double chance = dropChance * (attribute == null ? 1.0 : attribute.getValue() + 1.0);
        float num = entity.random.nextFloat();
        TextUtil.debugText(num + " v.s." + chance);
        Location location = entity.getBukkitEntity().getLocation().clone().add(0, 1, 0);
        if (num < chance) {
            if (dropChance <= 0.20) {
                dropRareItem(item, location, damageSource, dropChance);
            } else if (dropChance <= 0.50) {
                dropCommonItem(item, location, damageSource);
            } else {
                new ItemDisplay(location, item);
            }
        }
    }

    private static void dropRareItem(ItemStack item, Location location, DamageSource damageSource, double dropChance) {
        if (damageSource.getEntity() instanceof Player player) {
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) player.getBukkitLivingEntity();
            p.sendMessage(TextUtil.makeText("RARE DROP!   ", TextUtil.GOLD, true, true)
                    .append(TextUtil.makeText(TextUtil.getItemNameString(item), TextUtil.YELLOW))
                    .append(TextUtil.makeText("      (" +String.format("%.2f", dropChance * 100) + "%)", TextUtil.AQUA)));
        }
        new ItemDisplay(location, item);
    }

    private static void dropCommonItem(ItemStack item, Location location, DamageSource damageSource) {
        if (damageSource.getEntity() instanceof Player player) {
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) player.getBukkitLivingEntity();
            p.sendMessage(TextUtil.makeText("UNCOMMON DROP!   ", TextUtil.GREEN, true, false)
                    .append(TextUtil.makeText(TextUtil.getItemNameString(item), TextUtil.YELLOW)));
        }
        new ItemDisplay(location, item);
    }
}
