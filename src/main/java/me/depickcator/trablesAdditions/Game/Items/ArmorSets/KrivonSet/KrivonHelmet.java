package me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet;

import io.papermc.paper.tag.EntitySetTag;
import io.papermc.paper.tag.EntityTags;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorPiece;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorSet;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerArmorEffects;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.List;

public class KrivonHelmet extends KrivonArmorPiece implements ItemClick {
    private static KrivonHelmet instance;

    private KrivonHelmet() {
        super("Krivon Helmet", "krivon_helmet",  ArmorSet.HELMET_SLOT);
        registerClick(this, this);
    }

    @Override
    public void addStatArmorEffects(PlayerArmorEffects effects) {
        TextUtil.debugText("Added Krivon Helmet");
        effects.addResistance(EntityTags.UNDEADS, 0.03);
    }

    @Override
    public void removeStatArmorEffects(PlayerArmorEffects effects) {
        effects.removeResistance(EntityTags.UNDEADS, 0.03);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack armor = buildArmor(Material.PLAYER_HEAD, EquipmentSlotGroup.HEAD, 4, 3, 3);
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMThlND" +
                "MwNmEyYzFjZjZjNWVhOWUwMWViYmExY2ExZjU3MGQxNGE3Yzc5ZmE4ZmQxNjI1NzUwNGNjNzMxZTU2MSJ9fX0=";
        ItemUtil.moldHead(armor, base64);
        return armor;
    }

    public static KrivonHelmet getInstance() {
        if (instance == null) instance = new KrivonHelmet();
        return instance;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        Player p = e.getPlayer();
        ItemStack item = p.getEquipment().getItem(EquipmentSlot.HEAD);
        EquipmentSlot slot = e.getHand();
        p.getEquipment().setItem(EquipmentSlot.HEAD, e.getItem());
        p.getEquipment().setItem(slot, item);
        return false;
    }
}
