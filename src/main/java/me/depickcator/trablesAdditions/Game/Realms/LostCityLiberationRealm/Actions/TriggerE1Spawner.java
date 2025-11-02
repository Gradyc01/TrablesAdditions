package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.LCL_RangerSkeleton;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.LCL_ZombieKnight;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.LCL_ZombieVillager;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Entities.ItemDisplay;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class TriggerE1Spawner extends TrablesBlockGUI {
    private final ItemDisplay itemDisplay;
    private final LCL_Encounter1 encounter;
    private final Entity entity;
    private boolean hasBeenInteractedWith;
    public TriggerE1Spawner(Block block, LCL_Encounter1 encounter, ItemStack itemStack, List<Player> players) {
        super(block, 6, TextUtil.makeText(""));
        block.setBlockData(Material.BARRIER.createBlockData());
        this.encounter = encounter;
        this.hasBeenInteractedWith = false;
        Location location = block.getLocation().toCenterLocation();
        itemDisplay = new ItemDisplay(block.getLocation().toCenterLocation(), new ItemStack(Material.TRIAL_SPAWNER), players);
        itemDisplay.setGlowingTag(true);
        itemDisplay.setCustomNameVisible(false);
        location.add(0, 1, 0);
        entity = new Random().nextInt(0, 2) == 1 ?
                new LCL_ZombieKnight(location, itemStack) :
                new LCL_RangerSkeleton(location, itemStack);
    }

    @Override
    public boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event) {
        if (!hasBeenInteractedWith) {
            ((CraftWorld) block.getWorld()).getHandle().addFreshEntity(entity);
            TextUtil.errorMessage(playerData.getPlayer(), "What have you done");
        }
        this.hasBeenInteractedWith = true;
        return false;
    }

    public void remove() {
        itemDisplay.remove(Entity.RemovalReason.DISCARDED);
        getBlock().setBlockData(Material.AIR.createBlockData());
        TrablesBlockGUI.removeGUI(this);
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        return false;
    }



}
