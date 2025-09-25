package me.depickcator.trablesAdditions.Game.Items.WitherRealm;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.EntranceKey;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;

import java.util.List;

public class WitherRealmEntranceKey extends CustomItem implements ItemClick, EntranceKey {
    private static WitherRealmEntranceKey instance;

    private WitherRealmEntranceKey() {
        super("Wither Realm Portal Key", "wither_realm_portal_key");
        registerClick(this, this);
    }


    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWE0MzA" +
                "1NTBkNzc2YmU5MWI4ZWQ1MTg1M2ZlMzg4ZDU1MjYwMjU2YWMzMWY2OGU2N2RkNzJlNGY0NTg0M2M2MCJ9fX0=";
        ItemStack item = ItemUtil.buildHead(base64, TextUtil.makeText(getDisplayName(), TextUtil.YELLOW).append(TextUtil.placeText()),
                List.of(TextUtil.makeText("Place this to summon the Wither Realm",TextUtil.DARK_PURPLE)));
        addUnrepairable(item);
        generateUniqueModelString(item);
        return item;
    }

    public static WitherRealmEntranceKey getInstance() {
        if (instance == null) instance = new WitherRealmEntranceKey();
        return instance;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        if (e.getClickedBlock() == null || !e.getAction().isRightClick()) return false;
        Player player = pD.getPlayer();
        if (RealmController.getController(player.getWorld().getName()) != null) return false;
        TextUtil.debugText((int) (player.getLocation().getRotation().yaw() / 90) + " Direction");
        TextUtil.debugText((int) player.getLocation().getRotation().yaw() + " yaw");
        TextUtil.debugText(player.getLocation().getDirection().toString());
        TextUtil.debugText(player.getFacing().name());
//        return false;
        Vector v = e.getBlockFace().getDirection();
        if (v.getY() <= 0) return false;
        Block block = e.getClickedBlock().getRelative(e.getBlockFace());
        if (block.getType() != Material.AIR) {
            TextUtil.errorMessage(player, "The " + getDisplayName() + " cannot be placed here!");
            return false;
        }
        ItemStack item = e.getItem();
        if (item == null) return false;
        e.getItem().setAmount(e.getItem().getAmount() - 1);
        new WitherRealm(block.getLocation().add(0.5, 0, 0.5).setDirection(player.getLocation().getDirection())).initialize(pD);

        return false;
    }

    @Override
    public boolean canPurchase(PlayerData pD) {
//        return true;
        Player player = pD.getPlayer();
        PlayerInventory inv = player.getInventory();
        boolean coal = inv.contains(Material.COAL, 8);
        boolean iron_block =  inv.contains(Material.IRON_BLOCK, 1);
        if (coal && iron_block) {
            PlayerUtil.removeItems(player, new ItemStack(Material.COAL), 8);
            PlayerUtil.removeItems(player, new ItemStack(Material.IRON_BLOCK), 1);
            return true;
        };
        return false;
    }

    @Override
    public List<Component> description() {
        return List.of(TextUtil.makeText("Big Wither Man", TextUtil.DARK_PURPLE));
    }

    @Override
    public List<Component> purchaseRequirements() {
        return List.of(TextUtil.makeText(" 8 Coals", TextUtil.WHITE),
                        TextUtil.makeText(" 1 Iron Block", TextUtil.WHITE));
    }

    @Override
    public Component teamSize() {
        return TextUtil.makeText("2 - 6 Players", TextUtil.GREEN);
    }

    @Override
    public List<Component> requiredItems() {
        return List.of(TextUtil.makeText(" Moderate Armor", TextUtil.GREEN),
                TextUtil.makeText(" TNT", TextUtil.RED));
    }
}
