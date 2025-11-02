package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TrueSightKey extends CustomItem  {
    private static TrueSightKey instance;
    private TrueSightKey() {
        super("True Sight Icon", "true_sight_icon");
    }

    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJjYz" +
                "BmOGYyY2JmMzZkODJmZTBkNDhjMmNlM2I2MDM0OTRjYTk3OTczYmY2NjYzMTI3MjM2ODhhMmU1YzVlNCJ9fX0=";
        ItemStack item = ItemUtil.buildHead(base64, TextUtil.makeText(getDisplayName(), TextUtil.YELLOW), List.of());
        ItemMeta meta =  item.getItemMeta();
        meta.setMaxStackSize(1);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    public static TrueSightKey getInstance() {
        if (instance == null) {
            instance = new TrueSightKey();
        }
        return instance;
    }

}
