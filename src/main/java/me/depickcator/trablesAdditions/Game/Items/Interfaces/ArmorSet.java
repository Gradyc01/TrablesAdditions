package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerArmorEffects;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ArmorSet {
    public static int HELMET_SLOT = 0;
    public static int CHESTPLATE_SLOT = 1;
    public static int LEGGINGS_SLOT = 2;
    public static int BOOTS_SLOT = 3;
//    private final List<ArmorPiece> armorPieces;
    private final Map<Integer, ArmorPiece> armorPieces;
    private final String name;
    public ArmorSet(String name) {
        armorPieces = new HashMap<>();
//        armorPieces = new ArrayList<>(List.of(dummyPiece, dummyPiece, dummyPiece, dummyPiece));
        this.name = name;
    }

    public void setSlot(int slotNumber, ArmorPiece armorPiece) {
//        armorPieces.set(slotNumber, armorPiece);
        armorPieces.put(slotNumber, armorPiece);
    }

    public void checkForFullArmorSetBonus(PlayerArmorEffects effects) {
        for (String string : effects.getPlayerArmor()) {
            TextUtil.debugText("ArmorSet", string);
        }
        for (ArmorPiece piece : armorPieces.values()) {
            if (piece == null) continue;
            TextUtil.debugText("ArmorSet", ItemComparison.itemParser(piece.getResult()));
            if (!effects.getPlayerArmor().contains(ItemComparison.itemParser(piece.getResult()))) {
                TextUtil.debugText("Does not have");
                if (effects.hasArmorSetBonus()) {
                    effects.setNewArmorSetBonus(null);
                    removeFullArmorSetBonus(effects);
                }
                return;
            }
            TextUtil.debugText("Does have");
        }
        if (!effects.hasArmorSetBonus()) {
            effects.setNewArmorSetBonus(this);
            addFullArmorSetBonus(effects);
        }
    }

    public void triggerTemporaryDamageArmorSetEffects(PlayerArmorEffects effects, EntityDamageByEntityEvent event) {
        TextUtil.debugText("Armor Set " + getName(), "Triggered damage armor set effect");
    }
    protected abstract void removeFullArmorSetBonus(PlayerArmorEffects effects);
    protected abstract void addFullArmorSetBonus(PlayerArmorEffects effects);

    public abstract List<Component> getArmorSetBonusLore();

    public String getName() {
        return name;
    }
}
