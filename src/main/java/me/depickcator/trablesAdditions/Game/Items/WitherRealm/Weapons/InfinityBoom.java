package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CompactTNT;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ShatteredQuiver;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InfinityBoom extends Craft implements ItemClick {
    private static InfinityBoom instance;

    private InfinityBoom() {
        super("Infinity Boom", "infinity_boom");
        registerClick(this, this);
    }


    @Override
    protected Recipe initRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, getKey());
        ShapedRecipe recipe = new ShapedRecipe(key, getResult());
        recipe.shape("AAA", "ABA", "AAA");
        recipe.setIngredient('A', CompactTNT.getInstance().getResult());
        recipe.setIngredient('B', ShatteredQuiver.getInstance().getResult());
        return recipe;
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.TNT);
        ItemMeta meta =  item.getItemMeta();
        meta.customName(TextUtil.makeText(getDisplayName(), TextUtil.AQUA).append(TextUtil.rightClickText()));
        meta.lore(List.of(
                TextUtil.makeText("Born from the chaos of the Realms", TextUtil.DARK_PURPLE),
                TextUtil.makeText("this artifact pulses with endless fury", TextUtil.DARK_PURPLE),
                TextUtil.makeText("casting destruction without end", TextUtil.DARK_PURPLE)
        ));
        meta.setMaxStackSize(1);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        addUnrepairable(item);
        addCooldownGroup(item);
        return item;
    }

    public static InfinityBoom getInstance() {
        if (instance == null) instance = new InfinityBoom();
        return instance;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        if (e.getClickedBlock() == null || !e.getAction().isRightClick()) return false;
        Player player = pD.getPlayer();
        Block block = e.getClickedBlock().getRelative(e.getBlockFace());
        if (block.getType() != Material.AIR) {
            TextUtil.errorMessage(player, "The " + getDisplayName() + " cannot be placed here!");
            return false;
        }
        if (checkCooldown(player, e.getItem())) {
            TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(block.getLocation().add(0.5, 0, 0.5), EntityType.TNT);
            tnt.setYield(3);
        }
        return false;
    }

    private boolean checkCooldown(Player p, ItemStack item) {
        if (!p.hasCooldown(item) ) {
            p.setCooldown(item, 30 * 20);
            return true;
        }
        return false;
    }



}
