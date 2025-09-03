package me.depickcator.trablesAdditions.Game.Realms.SharedEntities.StartNPC;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Set;

public class StartingNPCGUI extends TrablesPlayerMenuGUI {
    private final ItemStack readyButton;
    private final StartingNPC startingNPC;
    public StartingNPCGUI(PlayerData playerData, StartingNPC startingNPC) {
        super(playerData, 6, TextUtil.makeText("Ready Menu", TextUtil.AQUA), true);
        readyButton = initReadyButton();
        this.startingNPC = startingNPC;
        initPanes();
        initSkulls();
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        if (event.getCurrentItem().equals(readyButton) && !startingNPC.playerHasReadied(playerData.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            startingNPC.addReadiedPlayer(playerData);
            player.closeInventory();
        }
        return false;
    }

    private void initSkulls() {
        int index = 20;
        World world = player.getWorld();
        for (Player player : world.getPlayers()) {
            inventory.setItem(index, initSkull(player));
            if (index++ % 9 == 7) index+=4;
        }
    }

    private void initPanes() {
        for (int index : Set.of(10, 11, 12, 13, 14, 15, 16, 19, 25, 28, 34, 37, 43, 46, 52)) {
            inventory.setItem(index, initExplainerItem(Material.LIME_STAINED_GLASS_PANE, List.of(), TextUtil.makeText("")));
        }
    }

    private ItemStack initSkull(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        meta.displayName(TextUtil.makeText(player.getName(), TextUtil.GOLD));
        List<Component> lore = startingNPC.playerHasReadied(player.getUniqueId()) ?
                List.of(TextUtil.makeText("Readied", TextUtil.GREEN)) :
                List.of(TextUtil.makeText("Not Ready", TextUtil.RED));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }


    private ItemStack initReadyButton() {
        ItemStack item = initExplainerItem(Material.GREEN_WOOL,
                List.of(TextUtil.makeText("Click Here when you are ready!", TextUtil.DARK_PURPLE)),
                TextUtil.makeText("Ready Up!", TextUtil.DARK_GREEN));
        inventory.setItem(49, item);
        return item;
    }
}
