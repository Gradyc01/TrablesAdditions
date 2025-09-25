package me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet;

import io.papermc.paper.tag.EntityTags;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorPiece;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorSet;
import me.depickcator.trablesAdditions.Game.Player.PlayerArmorEffects;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.ArrayList;
import java.util.List;

public class KrivonBoots extends KrivonArmorPiece {
    private static KrivonBoots instance;

    private KrivonBoots() {
        super("Krivon Boots", "krivon_boots", ArmorSet.BOOTS_SLOT);

    }

    @Override
    public void addStatArmorEffects(PlayerArmorEffects effects) {
        effects.addResistance(EntityTags.UNDEADS, 0.03);
    }

    @Override
    public void removeStatArmorEffects(PlayerArmorEffects effects) {
        effects.removeResistance(EntityTags.UNDEADS, 0.03);
    }

    @Override
    protected ItemStack initResult() {
        return buildArmor(Material.IRON_BOOTS, EquipmentSlotGroup.FEET, 3.5, 3, 3);
    }

    public static KrivonBoots getInstance() {
        if (instance == null) instance = new KrivonBoots();
        return instance;
    }
}
