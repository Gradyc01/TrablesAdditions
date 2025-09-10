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

public class OceanCore extends CustomItem implements ItemClick {
    private static OceanCore instance;
    private OceanCore() {
        super("Ocean Core", "ocean_core");
        registerItem(this, this);
    }

    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZThmN" +
                "WRjOTI3OTIzMjc5MTI3YTlkMmFkZTg2NDMyZjk4Nzc2MDljYjlmODM4NTRhNWI4OTJiZjdjYWQ5ZGYyZiJ9fX0=";
        ItemStack item = ItemUtil.buildHead(base64, TextUtil.makeText(getDisplayName(), TextUtil.AQUA), List.of(
                TextUtil.makeText("These disciple's were taken from the ocean", TextUtil.DARK_PURPLE),
                TextUtil.makeText("against their own will and enslaved into the ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("fight for Krivon. This holds their last memory", TextUtil.DARK_PURPLE),
                TextUtil.makeText("to the ocean", TextUtil.DARK_PURPLE)));
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

    public static OceanCore getInstance() {
        if (instance == null) {
            instance = new OceanCore();
        }
        return instance;
    }

}
