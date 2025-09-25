package me.depickcator.trablesAdditions.Game.Player;

import io.papermc.paper.tag.EntitySetTag;
import io.papermc.paper.tag.EntityTags;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorPiece;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorSet;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerArmorEffects {
    private final Player player;
    private final PlayerData playerData;
    private final TrablesAdditions plugin;
    private final Map<EntityType, Double> resistances;
    private ArmorSet armorSetBonus;

    public PlayerArmorEffects(PlayerData playerData) {
        this.playerData = playerData;
        this.player = playerData.getPlayer();
        resistances = new HashMap<>();
        this.plugin = TrablesAdditions.getInstance();
        armorSetBonus = null;
    }

    public double uponGettingDamaged(EntityDamageByEntityEvent event) {
        double dmg = event.getDamage();
        String debugText = "Old Damage " + String.format("%.1f", dmg) + "    ";
        DamageSource source = event.getDamageSource();
        if (source.getCausingEntity() != null) dmg *= getResistanceMultiplier(source.getCausingEntity().getType());
        debugText += " From Causing entity -> " +String.format("%.1f", dmg) + "    ";
        if (source.getDirectEntity() != null) dmg *= getResistanceMultiplier(source.getDirectEntity().getType());
        debugText += " From Direct entity -> " +String.format("%.1f", dmg) + "    ";
        TextUtil.debugText("Armor Effects " + player.getName(), debugText);
        for (ItemStack item : player.getEquipment().getArmorContents()) {
            if (item == null) continue;
            ArmorPiece armorPiece = plugin.getCraftData().findArmorPiece(item);
            if (armorPiece != null) armorPiece.triggerTemporaryDamageArmorEffects(this, event);
            if (armorSetBonus != null) armorSetBonus.triggerTemporaryDamageArmorSetEffects(this, event);
        }
        return dmg;
    }

    public double getResistance(EntityType type) {
        TextUtil.debugText("Armor Effects " + player.getName(), " Resistance against " + type.getKey().getKey() +
                " is now at " + resistances.getOrDefault(type, 0.0));
        return resistances.getOrDefault(type, 0.0);
    }

    public double getResistanceMultiplier(EntityType type) {
        return 1 - resistances.getOrDefault(type, 0.0);
    }

    public double addResistance(EntityType type, double amount) {
        double resistance = getResistance(type);
        resistances.put(type, resistance + amount);

        return getResistance(type);
    }

    public void addResistance(EntitySetTag set, double amount) {
        for (EntityType type : set.getValues()) {
            addResistance(type, amount);
        }
    }

    public double removeResistance(EntityType type, double amount) {
        double resistance = getResistance(type);
        resistances.put(type, Double.max(0.0, resistance - amount));
        return getResistance(type);
    }

    public void removeResistance(EntitySetTag set, double amount) {
        for (EntityType type : set.getValues()) {
            removeResistance(type, amount);
        }
    }

    public List<String> getPlayerArmor() {
        List<String> itemStrings = new ArrayList<>();
        for (ItemStack item : playerData.getPlayer().getEquipment().getArmorContents()) {
            if (item == null) continue;
            itemStrings.add(ItemComparison.itemParser(item));
        }
        return itemStrings;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public boolean hasArmorSetBonus() {
        return armorSetBonus != null;
    }

    public void setNewArmorSetBonus(ArmorSet armorSetBonus) {
        this.armorSetBonus = armorSetBonus;
    }
}
