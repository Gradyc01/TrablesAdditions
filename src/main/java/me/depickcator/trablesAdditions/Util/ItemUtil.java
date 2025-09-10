package me.depickcator.trablesAdditions.Util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class ItemUtil {
    public static ItemStack buildHead(String base64, Component name, List<Component> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.fromString("5f856526-a7c6-4782-bcf9-803e02b08e1d"), null);
        profile.getProperties().add(new ProfileProperty("textures", base64));
        meta.setPlayerProfile(profile);
        meta.displayName(name);
        meta.lore(lore);
        item.setItemMeta(meta);
        Repairable meta1 = (Repairable) item.getItemMeta();
        meta1.setRepairCost(999);
        item.setItemMeta(meta1);
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
}
