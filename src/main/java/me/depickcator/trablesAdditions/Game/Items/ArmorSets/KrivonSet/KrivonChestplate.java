package me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet;

import io.papermc.paper.tag.EntityTags;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorPiece;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorSet;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerArmorEffects;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.List;

public class KrivonChestplate extends KrivonArmorPiece{
    private static KrivonChestplate instance;

    private KrivonChestplate() {
        super("Krivon Chestplate", "krivon_chestplate", ArmorSet.CHESTPLATE_SLOT);

    }

    @Override
    public void addStatArmorEffects(PlayerArmorEffects effects) {
        TextUtil.debugText("Added Krivon Chestplate");
        effects.addResistance(EntityTags.UNDEADS, 0.05);
    }

    @Override
    public void removeStatArmorEffects(PlayerArmorEffects effects) {
        effects.removeResistance(EntityTags.UNDEADS, 0.05);
    }

    @Override
    protected ItemStack initResult() {
        return buildArmor(Material.IRON_CHESTPLATE, EquipmentSlotGroup.CHEST, 7, 3, 5);
    }
    public static KrivonChestplate getInstance() {
        if (instance == null) instance = new KrivonChestplate();
        return instance;
    }
}
