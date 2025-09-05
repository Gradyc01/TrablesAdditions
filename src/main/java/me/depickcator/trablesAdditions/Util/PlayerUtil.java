package me.depickcator.trablesAdditions.Util;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class PlayerUtil {
    private static final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public static void assignNewPlayerData() {
        playerDataMap.clear();
        List<Player> onlinePlayerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player p : onlinePlayerList) {
            playerDataMap.put(p.getUniqueId(), new PlayerData(p));
        }
    }

    public static PlayerData assignNewPlayerData(Player p) {
        PlayerData playerData = new PlayerData(p);
        playerDataMap.put(p.getUniqueId(), playerData);
        return playerData;
    }

    public static void clearPlayerDataMap() {
        playerDataMap.clear();
    }

    public static PlayerData getPlayerData(Player p) {
        return Objects.requireNonNull(getPlayerData(p, false));
    }

    public static PlayerData getPlayerData(Player p, boolean nullable) {
        if (playerDataMap.containsKey(p.getUniqueId())) {
            return playerDataMap.get(p.getUniqueId());
        }
        if (nullable) {
            TextUtil.debugText("PlayerUtil", "Player Data not found! Returning null...");
            return null;
        } else {
            TextUtil.debugText("PlayerUtil", "Player Data not found! Creating new one...");
            return assignNewPlayerData(p);
        }
    }

    public static boolean removePlayerData(Player p) {
        if (playerDataMap.containsKey(p.getUniqueId())) {
            PlayerData playerData = playerDataMap.get(p.getUniqueId());
            playerData.saveData();
            playerDataMap.remove(p.getUniqueId());
            return true;
        }
        return false;
    }

    public static Collection<PlayerData> getAllPlayerData() {
        return playerDataMap.values();
    }

    public static void giveItem(Player p, ItemStack... items) {
        giveItem(p, new ArrayList<>(List.of(items)));
    }

    public static void giveItem(Player p, List<ItemStack> items) {
        PlayerInventory inv = p.getInventory();
        List<ItemStack> itemsLeft = new ArrayList<>(); // Items Left to give
        for (ItemStack item : items) {
            itemsLeft.add(item.clone());
        }
        List<Integer> emptySlots = new ArrayList<>(); // All empty slots
        for (int i = 0; i < 35; i++) {
            ItemStack invItem = inv.getItem(i);
            if (invItem == null) {
                emptySlots.add(i);
            } else {
                for (ItemStack item : new ArrayList<>(itemsLeft)) {;
                    if (ItemComparison.equalItems(item, invItem)) {
                        int maxSize = invItem.getMaxStackSize();
                        int itemAmount = item.getAmount();
                        int invItemAmount = invItem.getAmount();
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 2);
                        if (maxSize >= invItemAmount + itemAmount) {
                            invItem.setAmount(invItemAmount + itemAmount);
                            itemsLeft.remove(item);
                        } else {
                            invItem.setAmount(maxSize);
                            item.setAmount(itemAmount - (maxSize - invItemAmount));
                            //Adds to tracker and recommender
                            if (maxSize - invItemAmount > 0) {
                            }
                        }
                    }
                }
            }
        }
        boolean dropped = false;
        for (ItemStack item : itemsLeft) {
            try {
                int slot = emptySlots.getFirst();
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 2);
                inv.setItem(slot, item);
                emptySlots.removeFirst();

            } catch (NoSuchElementException ignored) {
                dropped = true;
                p.getWorld().dropItem(p.getLocation(), item);
            }
        }
        if (dropped) {
            p.sendMessage(TextUtil.makeText("Some items were dropped onto the floor make sure you pick those up", TextUtil.RED));
        }
    }

    public static void clearEffects(PlayerData pD) {
        Player p = pD.getPlayer();
        p.clearActivePotionEffects();
    }
}
