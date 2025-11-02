package me.depickcator.trablesAdditions.Game.Player.PlayerStates;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public interface PlayerState {
    boolean onTeleport(PlayerData pD, PlayerTeleportEvent event);
    boolean onChangeEquipment(PlayerData pD, EntityEquipmentChangedEvent event);
    boolean onPlaceBlocks(PlayerData pD, BlockPlaceEvent event);
    boolean onPlayerInteract(PlayerData pD, PlayerInteractEvent event);
    boolean onPlayerDamageEntity(PlayerData pD, EntityDamageByEntityEvent event);
    boolean onPlayerDamagedByEntity(PlayerData pD, EntityDamageByEntityEvent event);
    boolean onPlayerInventory(PlayerData pD, InventoryClickEvent event);
    boolean onConsumeItem(PlayerData pD, PlayerItemConsumeEvent event);
    String getStateName();
    void onSet(PlayerData pD);
    void onRemove(PlayerData pD);
}
