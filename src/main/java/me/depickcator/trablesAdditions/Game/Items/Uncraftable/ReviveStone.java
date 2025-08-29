package me.depickcator.trablesAdditions.Game.Items.Uncraftable;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;


public class ReviveStone extends CustomItem implements ItemClick {
    private static ReviveStone instance;
    private ReviveStone() {
        super("Revive Stone", "revive_stone");
        registerItem(this, this);
    }

    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZhNzZj" +
                "YzIyZTdjMmFiOWM1NDBkMTI0NGVhZGJhNTgxZjVkZDllMThmOWFkYWNmMDUyODBhNWI0OGI4ZjYxOCJ9fX0=";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.fromString("5f856526-a7c6-4782-bcf9-803e02b08e1d"), null);
        profile.getProperties().add(new ProfileProperty("textures", base64));
        meta.setPlayerProfile(profile);
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.GOLD).append(TextUtil.rightClickText()));
        meta.lore(List.of(TextUtil.makeText("Revives a player from the dead to your position", TextUtil.DARK_PURPLE)));
        meta.setMaxStackSize(1);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }


    public ItemStack getResult(World world) {
        return super.getResult();
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        Player p = pD.getPlayer();
        e.setCancelled(true);

        RealmController controller = RealmController.getController(p.getWorld().getName());
        if (controller == null) return false;
        if (ItemComparison.isHolding(p, getResult()) && controller.getRealmPlayers().attemptToRevive(p)) {
            ItemStack item = p.getInventory().getItemInMainHand();
            item.setAmount(item.getAmount() - 1);
            return true;
        }
        return false;
    }


    public static ReviveStone getInstance() {
        if (instance == null) {
            instance = new ReviveStone();
        }
        return instance;
    }
}
