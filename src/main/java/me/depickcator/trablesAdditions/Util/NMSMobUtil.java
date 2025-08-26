package me.depickcator.trablesAdditions.Util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

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
}
