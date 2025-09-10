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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class ZombieHeart extends CustomItem implements ItemClick {
    private static ZombieHeart instance;
    private ZombieHeart() {
        super("Zombie Heart", "zombie_heart");
        registerItem(this, this);
    }

    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzF" +
                "kN2M4MTZmYzhjNjM2ZDdmNTBhOTNhMGJhN2FhZWZmMDZjOTZhNTYxNjQ1ZTllYjFiZWYzOTE2NTVjNTMxIn19fQ==";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.fromString("5f856526-a7c6-4782-bcf9-803e02b08e1d"), null);
        profile.getProperties().add(new ProfileProperty("textures", base64));
        meta.setPlayerProfile(profile);
        meta.setEnchantmentGlintOverride(true);
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.WHITE));
        meta.lore(List.of(
                TextUtil.makeText("The heart of a dungeon zombie", TextUtil.DARK_PURPLE),
                TextUtil.makeText("ripped out of their flesh", TextUtil.DARK_PURPLE)
        ));
        meta.setMaxStackSize(1);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        return e.getPlayer().isSneaking();
    }

    public static ZombieHeart getInstance() {
        if (instance == null) {
            instance = new ZombieHeart();
        }
        return instance;
    }
}
