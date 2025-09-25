package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.WitherRealmKey;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealmLoot;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class WitherRealm_PrizeLootGUI extends TrablesPlayerMenuGUI {
    private final ItemStack claimItem;
    private final Collection<ItemStack> loot;
    private final List<Integer> barrier = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44,
            45, 46, 47, 48, 50, 51, 52, 53);
    private boolean isClaimed;
     public WitherRealm_PrizeLootGUI(Player player, WitherRealmLoot loot, Random random) {
        super(PlayerUtil.getPlayerData(player), 6, TextUtil.makeText(loot.getTierName() + " Chest", TextUtil.GOLD),
                false, false);
        addBackground();
        this.claimItem = initClaimItem();
        this.loot = loot.populateLoot(inventory, random, 1.0).stream()
                .filter(itemStack -> itemStack.getType() != Material.AIR).collect(Collectors.toList());
        this.isClaimed = false;
    }

    private void addBackground() {
         ItemStack item = initExplainerItem(Material.GRAY_STAINED_GLASS_PANE, List.of(), TextUtil.makeText(""));
         for (int index : barrier) {
             inventory.setItem(index, item);
         }
    }

    private ItemStack initClaimItem() {
        ItemStack item = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText("Claim Chest", TextUtil.GREEN));
        meta.lore(List.of(
                TextUtil.makeText("Cost: ", TextUtil.GOLD),
                TextUtil.makeText("[1 " + WitherRealmKey.getInstance().getDisplayName() + "]", TextUtil.YELLOW),
                TextUtil.makeText(""),
                TextUtil.makeText("Claim this as your final ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("reward for beating this Realm", TextUtil.DARK_PURPLE)));
        item.setItemMeta(meta);
        inventory.setItem(49, item);
        return item;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return false;
        Player player = playerData.getPlayer();
        if (item.equals(claimItem) && PlayerUtil.containsItem(player, WitherRealmKey.getInstance().getResult(), 1)) {
            if (!PlayerUtil.removeItems(player, WitherRealmKey.getInstance().getResult(), 1)) return false;
            event.setCancelled(true);
            player.closeInventory();
            PlayerUtil.giveItem(player, loot);
            this.isClaimed = true;
        }
        return false;
    }

    public boolean isClaimed() {
        return isClaimed;
    }
}
