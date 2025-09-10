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

public class ThunderCore extends CustomItem implements ItemClick {
    private static ThunderCore instance;
    private ThunderCore() {
        super("Thunder Core", "thunder_core");
    }

    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWYwN" +
                "WExMzg1MzczNGI2YTdlNGY0NWVhYjcyYmI5ZGJjYzkxN2U3ODBjZDFlYjZkNjMwNDA1ZmE4Mjk5NjY1In19fQ==";
        ItemStack item = ItemUtil.buildHead(base64, TextUtil.makeText(getDisplayName(), TextUtil.AQUA), List.of(
                TextUtil.makeText("A relic from the trident infused with the storm's wrath.  ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("It crackles with ancient energy, amplifying lightning ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("strikes and channeling the fury of the deep", TextUtil.DARK_PURPLE)));
        ItemMeta meta =  item.getItemMeta();
        meta.setMaxStackSize(1);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        return e.getPlayer().isSneaking();
    }

    public static ThunderCore getInstance() {
        if (instance == null) {
            instance = new ThunderCore();
        }
        return instance;
    }
}
