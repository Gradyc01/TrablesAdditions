package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class SpiderSilk extends CustomItem implements ItemClick {
    private static SpiderSilk instance;
    private SpiderSilk() {
        super("Spider Silk", "spider_silk");
        registerItem(this, this);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.STRING);
        ItemMeta meta = item.getItemMeta();
        meta.setEnchantmentGlintOverride(true);
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.WHITE));
        meta.lore(List.of(
                TextUtil.makeText("Silk created only from ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("Wither Realm Spiders", TextUtil.DARK_PURPLE)
        ));
        meta.setMaxStackSize(16);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        e.setCancelled(true);
        return false;
    }

    public static SpiderSilk getInstance() {
        if (instance == null) {
            instance = new SpiderSilk();
        }
        return instance;
    }
}
