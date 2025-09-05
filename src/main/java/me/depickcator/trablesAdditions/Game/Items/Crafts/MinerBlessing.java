package me.depickcator.trablesAdditions.Game.Items.Crafts;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;

public class MinerBlessing extends Craft implements ItemClick {
    private static MinerBlessing instance;
    private MinerBlessing() {
        super("Miner's Blessing", "miner_blessing");
        registerItem(this, this);
    }

    public static MinerBlessing getInstance() {
        if (instance == null) instance = new MinerBlessing();
        return instance;
    }

    private RecipeChoice makeRecipeChoice() {
        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack item = QuickPick.getInstance().getResult();

        for (int i = 0; i < 250; i++) {
            ItemStack pick = item.clone();
            Damageable meta = (Damageable) pick.getItemMeta();
            meta.setDamage(i);
            pick.setItemMeta(meta);
            itemStacks.add(pick);
        }
        return new RecipeChoice.ExactChoice(itemStacks);
    }

    protected Recipe initRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, getKey());

        ShapedRecipe recipe = new ShapedRecipe(key, getResult());
        recipe.shape("AB ", "BC ", "  D");
        recipe.setIngredient('A', Material.TNT);
        recipe.setIngredient('B', Material.DIAMOND);
        recipe.setIngredient('C', makeRecipeChoice());
        recipe.setIngredient('D', Material.PLAYER_HEAD);
        return recipe;
    }

    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.YELLOW).append(TextUtil.rightClickText()));
        meta.addEnchant(Enchantment.EFFICIENCY, 3, true);
        meta.addEnchant(Enchantment.UNBREAKING, 2, true);
        meta.setMaxDamage(2500);
        List<Component> lore = new ArrayList<>(List.of(
                TextUtil.makeText("Launches TNT at the cost", TextUtil.DARK_PURPLE),
                TextUtil.makeText(" of pickaxe durability", TextUtil.DARK_PURPLE)
        ));
        meta.lore(lore);
        item.setItemMeta(meta);
        addCooldownGroup(item);
        generateUniqueModelString(item);
        return item;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        if (!e.getAction().isRightClick()) return false;
        ItemStack item = e.getItem();
        Damageable meta = (Damageable) item.getItemMeta();
        Player p = pD.getPlayer();
        if (checkCooldown(p, item)) {
            if (meta.getDamage() + 100 >= meta.getMaxDamage()) {
                item.setAmount(0);
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            } else {
                meta.setDamage(meta.getDamage() + 100);
                item.setItemMeta(meta);
            }
            launchTNT(p);
            return true;
        }
        return false;
    }

    /*Returns true if not on cooldown and sets the cooldown, False otherwise*/
    private boolean checkCooldown(Player p, ItemStack item) {
        if (!p.hasCooldown(item) ) {
            p.setCooldown(item, 12 * 20);
            return true;
        }
        return false;
    }

    private void launchTNT(Player p) {
        World world = p.getWorld();
        Location loc = p.getEyeLocation();
        TNTPrimed tnt = (TNTPrimed) world.spawnEntity(loc, EntityType.TNT);
        tnt.setFuseTicks(2 * 20);
        tnt.setYield(7);
        tnt.setSource(p);
        tnt.setVelocity(loc.getDirection().normalize().multiply(1.8));
        world.playSound(loc, Sound.ENTITY_WITHER_SPAWN, 2f, 2f);
        world.playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 1f, 1f);
    }

}
