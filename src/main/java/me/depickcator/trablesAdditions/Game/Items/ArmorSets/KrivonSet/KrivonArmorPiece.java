package me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet;

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

import java.util.List;

public abstract class KrivonArmorPiece extends ArmorPiece {
    public KrivonArmorPiece(String displayName, String key, int armorSlot) {
        super(displayName, key, KrivonArmorSet.getInstance(), armorSlot);
    }

    @Override
    public void triggerTemporaryDamageArmorEffects(PlayerArmorEffects effects, EntityDamageByEntityEvent event) {

    }

    protected ItemStack buildArmor(Material material, EquipmentSlotGroup slotGroup, double armor, int toughness, int resPercentage) {
        ItemStack item = makeArmor(material, 666, armor, toughness, slotGroup,
                TextUtil.makeText(getDisplayName(), TextUtil.YELLOW), List.of());
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();
        lore.add(TextUtil.makeText(" " + resPercentage + "% Undead Resistance", TextUtil.BLUE));
        lore.add(TextUtil.makeText(""));
        lore.addAll(KrivonArmorSet.getInstance().getArmorSetBonusLore());
        meta.lore(lore);
        item.setItemMeta(meta);
        addArmorTrim(item, TrimMaterial.NETHERITE, TrimPattern.RIB);
        return item;
    }

}
