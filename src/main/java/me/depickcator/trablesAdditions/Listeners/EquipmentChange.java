package me.depickcator.trablesAdditions.Listeners;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorPiece;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EquipmentChange extends TrablesListeners {
    private final TrablesAdditions plugin =  TrablesAdditions.getInstance();
    @EventHandler
    public void onEquipmentChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
//        boolean checkForWeaponChange = false;
//        boolean checkForArmorChange = false;

        PlayerData pD = PlayerUtil.getPlayerData(player, true);
        if (pD == null) return; //TODO: Shitty solve caused by changing inv when player leaves
        for (Map.Entry<EquipmentSlot, EntityEquipmentChangedEvent.EquipmentChange> entry : event.getEquipmentChanges().entrySet()) {
            EquipmentSlot slot = entry.getKey();
            EntityEquipmentChangedEvent.EquipmentChange change = entry.getValue();
            if (slot == EquipmentSlot.HAND || slot == EquipmentSlot.OFF_HAND &&
                    !ItemComparison.equalItems(change.newItem(), change.oldItem())) {

//                checkForWeaponChange = true;
            } else if (!ItemComparison.equalItems(change.newItem(), change.oldItem())) {
//                checkForArmorChange = true;
                checkForArmorChange(pD, change.oldItem(), change.newItem());
            }
        }

//        if (checkForWeaponChange) checkForWeaponChange(pD);
//        if (checkForArmorChange) checkForArmorChange(pD);
    }

    private void checkForWeaponChange(PlayerData playerData) {
//        playerData.getPlayer().sendMessage(TextUtil.makeText("Weapon has changed"));
    }

    private void checkForArmorChange(PlayerData playerData, ItemStack oldItem, ItemStack newItem) {
        ArmorPiece oldPiece = plugin.getCraftData().findArmorPiece(oldItem);
        ArmorPiece newPiece = plugin.getCraftData().findArmorPiece(newItem);
        TextUtil.debugText(oldItem.getType() + "     "  + newItem.getType());
        if (oldPiece != null) oldPiece.removeStatArmor(playerData.getPlayerArmorEffects());
        if (newPiece != null) newPiece.addStatArmor(playerData.getPlayerArmorEffects());
    }
}
