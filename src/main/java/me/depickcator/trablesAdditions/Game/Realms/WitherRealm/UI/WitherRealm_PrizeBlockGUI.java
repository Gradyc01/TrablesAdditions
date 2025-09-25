package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealmLoot;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class WitherRealm_PrizeBlockGUI extends WitherRealm_LootGUI {
//    private final List<Player> players;
    private final Map<UUID, WitherRealm_PrizeLootGUI> map;
    public WitherRealm_PrizeBlockGUI(Block block, WitherRealmLoot loot, Random random, List<Player> players) {
        super(block, loot, random, null);
        map = buildMap(players, loot, random);

    }

    private Map<UUID, WitherRealm_PrizeLootGUI> buildMap(List<Player> players, WitherRealmLoot loot, Random random) {
        Map<UUID, WitherRealm_PrizeLootGUI> map = new HashMap<>();
        for (Player player : players) {
            map.put(player.getUniqueId(), new WitherRealm_PrizeLootGUI(player, loot, random));
        }
        return map;
    }

    @Override
    public boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event) {
        if (event.getAction().isRightClick() && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
//            open(playerData.getPlayer());
//            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
            Player player = playerData.getPlayer();
            if (map.containsKey(player.getUniqueId())) {
                WitherRealm_PrizeLootGUI gui = map.get(event.getPlayer().getUniqueId());
                if (gui.isClaimed()) {
                    TextUtil.errorMessage(player, "You've already claimed this chest!");
                    return false;
                }
                gui.open(player);
            }
        }
        return false;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        return false;
    }
}
