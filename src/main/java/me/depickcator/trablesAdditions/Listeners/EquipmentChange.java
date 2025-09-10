package me.depickcator.trablesAdditions.Listeners;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;

public class EquipmentChange extends TrablesListeners {
    @EventHandler
    public void onEquipmentChange(EntityEquipmentChangedEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        boolean checkForWeaponChange = false;
        boolean checkForArmorChange = false;

        PlayerData pD = PlayerUtil.getPlayerData(player);
        for (Map.Entry<EquipmentSlot, EntityEquipmentChangedEvent.EquipmentChange> entry : event.getEquipmentChanges().entrySet()) {
            EquipmentSlot slot = entry.getKey();
            EntityEquipmentChangedEvent.EquipmentChange change = entry.getValue();
            if (slot == EquipmentSlot.HAND || slot == EquipmentSlot.OFF_HAND &&
                    !ItemComparison.equalItems(change.newItem(), change.oldItem())) {
                checkForWeaponChange = true;
            } else if (!ItemComparison.equalItems(change.newItem(), change.oldItem())) {
                checkForArmorChange = true;
            }
        }

        if (checkForWeaponChange) checkForWeaponChange(pD);
        if (checkForArmorChange) checkForArmorChange(pD);
    }

    private void checkForWeaponChange(PlayerData playerData) {
//        playerData.getPlayer().sendMessage(TextUtil.makeText("Weapon has changed"));
    }

    private void checkForArmorChange(PlayerData playerData) {
        playerData.getPlayer().sendMessage(TextUtil.makeText("Armor has changed"));
    }
}
