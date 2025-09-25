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

public class KrivonLeggings extends KrivonArmorPiece {
    private static KrivonLeggings instance;

    private KrivonLeggings() {
        super("Krivon Leggings", "krivon_leggings", ArmorSet.LEGGINGS_SLOT);

    }
    @Override
    public void addStatArmorEffects(PlayerArmorEffects effects) {
        effects.addResistance(EntityTags.UNDEADS, 0.04);
    }

    @Override
    public void removeStatArmorEffects(PlayerArmorEffects effects) {
        effects.removeResistance(EntityTags.UNDEADS, 0.04);
    }

    @Override
    protected ItemStack initResult() {
        return buildArmor(Material.IRON_LEGGINGS, EquipmentSlotGroup.LEGS, 5, 3, 4);
    }

    public static KrivonLeggings getInstance() {
        if (instance == null) instance = new KrivonLeggings();
        return instance;
    }
}
