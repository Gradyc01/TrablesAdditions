package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemCooldown {
    boolean checkCooldown(Player player, ItemStack item);
}
