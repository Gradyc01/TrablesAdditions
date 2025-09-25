package me.depickcator.trablesAdditions.Game.Items.Uncraftable;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;


public class XPTome extends CustomItem implements ItemClick {
    private static XPTome instance;
    private final static NamespacedKey key = new NamespacedKey(TrablesAdditions.getInstance(), "xp_tome");
    private final int MAX_STORED = 1600;
    private XPTome() {
        super("XP Tome", "xp_tome");
        registerClick(this, this);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item =  new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.customName(TextUtil.makeText(getDisplayName(), TextUtil.DARK_GREEN).append(TextUtil.rightClickText()));
        meta.lore(List.of(
                TextUtil.makeText(""),
                TextUtil.makeText("Stores experience in this book", TextUtil.DARK_PURPLE),
                TextUtil.makeText("so it may be used at a later point", TextUtil.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        singleStack(item);
        addUnrepairable(item);
        return item;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        Player player = pD.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) return false;
        int storedExp = getStoredExperience(item);
        if (e.getAction().isRightClick()) {
            if (player.isSneaking()) {
                if (storedExp < MAX_STORED) {
                    int playerExp = player.calculateTotalExperiencePoints();
                    if (playerExp == 0) return false;
                    int addExp = Integer.min(MAX_STORED - storedExp, playerExp);
                    player.setExperienceLevelAndProgress(playerExp - addExp);
                    storedExp += addExp;
                    updateStoredExpTag(item, storedExp);
                    player.playSound(SoundUtil.makeSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0));
                } else {
                    TextUtil.errorMessage(player, "This " + getDisplayName() +" has already been maxed out");
                }
            } else {
                if (storedExp > 0) {
                    player.giveExp((int) (storedExp * 0.9), true);
                    updateStoredExpTag(item, 0);
                } else {
                    TextUtil.errorMessage(player, "This " + getDisplayName() +" has no experience");
                }
            }
        }

        return false;
    }

    private void updateStoredExpTag(ItemStack item, int storedExp) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.INTEGER, storedExp);
        meta.setEnchantmentGlintOverride(storedExp > 0);
        List<Component> lore = meta.lore();
        lore.set(0, TextUtil.makeText(storedExp + "/" + MAX_STORED + " Exp Stored", TextUtil.YELLOW));
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    private int getStoredExperience(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(key, PersistentDataType.INTEGER)) {
            return container.get(key, PersistentDataType.INTEGER);
        }
        container.set(key, PersistentDataType.INTEGER, 0);
        return 0;
    }


    public static XPTome getInstance() {
        if (instance == null) {
            instance = new XPTome();
        }
        return instance;
    }

    /*amount here is the amount of xp the user will get*/
    @Override
    public ItemStack getResult(int amount) {
        ItemStack item = this.getResult();
        int newAmount = amount == -1 ? new Random().nextInt(MAX_STORED) : amount;
        updateStoredExpTag(item, newAmount);
        return item;
    }
}
