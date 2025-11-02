package me.depickcator.trablesAdditions.Game.Player.PlayerStates;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public abstract class PlayerAbstractState implements PlayerState {

    @Override
    public boolean onTeleport(PlayerData pD, PlayerTeleportEvent event) {
        return true;
    }

    @Override
    public boolean onChangeEquipment(PlayerData pD, EntityEquipmentChangedEvent event) {
        return true;
    }

    @Override
    public boolean onPlaceBlocks(PlayerData pD, BlockPlaceEvent event) {
        return true;
    }

    @Override
    public boolean onPlayerInteract(PlayerData pD, PlayerInteractEvent event) {
        return true;
    }

    @Override
    public boolean onPlayerDamageEntity(PlayerData pD, EntityDamageByEntityEvent event) {
        return true;
    }

    @Override
    public boolean onPlayerInventory(PlayerData pD, InventoryClickEvent event) {
        return true;
    }

    @Override
    public boolean onConsumeItem(PlayerData pD, PlayerItemConsumeEvent event) {
        return true;
    }

    @Override
    public boolean onPlayerDamagedByEntity(PlayerData pD, EntityDamageByEntityEvent event) {
        return true;
    }

    @Override
    public void onRemove(PlayerData pD) {
        //Nothing on Purpose
    }

    @Override
    public void onSet(PlayerData pD) {
        //Nothing on Purpose
    }
}
