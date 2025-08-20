package me.depickcator.trablesAdditions.Util;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;

public class AttributeUtil {
    public static AttributeModifier makeModifier(NamespacedKey key, double amount, AttributeModifier.Operation operation, EquipmentSlotGroup slot) {
        return new AttributeModifier(key, amount, operation, slot );
    }
}
