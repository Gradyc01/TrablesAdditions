package me.depickcator.trablesAdditions.Util;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import java.util.List;

public class DisplayUtil {
    public static ArmorStand makeMarkerArmorStand(Location loc, World world, Component name) {
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.setGravity(false);         // Prevents the armor stand from falling
        armorStand.setVisible(false);         // Hides the armor stand body
        armorStand.customName(name);
        armorStand.setInvisible(true);
        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        armorStand.setSilent(true);
        armorStand.setCustomNameVisible(true);
        return armorStand;
    }


    public static TextDisplay makeTextDisplay(Location loc, List<Component> text, float pitch, float yaw, int width) {
        TextDisplay e = (TextDisplay) loc.getWorld().spawnEntity(loc, EntityType.TEXT_DISPLAY);
        e.setGravity(false);
        e.setCustomNameVisible(false);
        e.setInvulnerable(true);
        e.setSilent(true);
        e.setPersistent(true);
        Component t = TextUtil.makeText("");
        for (Component c : text) {
            t = t.append(TextUtil.makeText("\n").append(c));
        }
//        e.setDisplayWidth(width);
        e.setAlignment(TextDisplay.TextAlignment.CENTER);
        e.setRotation(pitch, yaw);
        e.setDefaultBackground(false);
        e.setLineWidth(width);
        e.text(t);
//        e.setBillboard(Display.Billboard.HORIZONTAL);


        return e;
    }

    public static ItemDisplay makeItemDisplay(Location loc, ItemStack item, float pitch, int yaw, double size) {
        ItemDisplay e = (ItemDisplay) loc.getWorld().spawnEntity(loc, EntityType.ITEM_DISPLAY);
        e.setGravity(false);
        e.setCustomNameVisible(false);
        e.setInvulnerable(true);
        e.setSilent(true);
        Transformation t = e.getTransformation();
        t.getScale().set(size);
        e.setTransformation(t);
        e.setItemStack(item);
        e.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
        e.setRotation(yaw, pitch);
        return e;
    }
}
