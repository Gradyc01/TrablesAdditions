package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CompactTNT extends CustomItem implements ItemClick {
    private static CompactTNT instance;
    private CompactTNT() {
        super("Compact TNT", "compact_tnt");
        registerItem(this, this);
    }

    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0" +
                "L3RleHR1cmUvNzFiMGVjZWZlYzJhZDY4MTg5MTc4MjA4ZWEzYzkxZTVhNDdhZTk2NTY4ZWUzNzIwMjM3MTQwZGJlYzc2NTA1MCJ9fX0=";
        ItemStack item = ItemUtil.buildHead(base64, TextUtil.makeText(getDisplayName(), TextUtil.AQUA), List.of(
                        TextUtil.makeText("A tightly packed explosive so volatile", TextUtil.DARK_PURPLE),
                        TextUtil.makeText("even placing it could be disastrous ", TextUtil.DARK_PURPLE)));
        ItemMeta meta =  item.getItemMeta();
        meta.setMaxStackSize(4);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    public static CompactTNT getInstance() {
        if (instance == null) {
            instance = new CompactTNT();
        }
        return instance;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
//        if (!e.getPlayer().isSneaking()) e.setCancelled(true);
//        return false;
        return e.getPlayer().isSneaking();
    }
}
