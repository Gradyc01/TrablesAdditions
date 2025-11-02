package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates.LCL_E1_Spawners;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.LCL_RangerSkeleton;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.LCL_ZombieKnight;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Entities.ItemDisplay;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.Util.DisplayUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.world.entity.Entity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class TriggerAnswerPlacement extends TrablesBlockGUI {
    private ItemDisplay itemDisplay;
    private final TextDisplay textDisplay;
    private final LCL_Encounter1 encounter;
    private boolean hasBeenInteractedWith;
    public TriggerAnswerPlacement(Block block, LCL_Encounter1 encounter) {
        super(block, 6, TextUtil.makeText(""));
        this.encounter = encounter;
        this.hasBeenInteractedWith = false;
        Location location = block.getLocation().clone().toCenterLocation().add(0, 1, 0);
        textDisplay = DisplayUtil.makeTextDisplay(
                location,
                List.of(TextUtil.makeText("Key Required", TextUtil.GOLD)),
                0, 0, 100);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));

    }

    @Override
    public boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event) {
        if (!event.hasItem() || hasBeenInteractedWith) return false;
        ItemStack itemStack = event.getItem();
        if (!encounter.getSymbols().contains(itemStack)) return false;
        if (encounter.getRealmStates() instanceof LCL_E1_Spawners) {
            TextUtil.errorMessage(playerData.getPlayer(), " You can't do that right now!");
            return false;
        }
        if (encounter.isCorrectAnswer(itemStack)) {
            this.hasBeenInteractedWith = true;
            Location loc = getBlock().getLocation().add(0, 1, 0).toCenterLocation();
            loc.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 100, 0f);
            itemDisplay = new ItemDisplay(loc, itemStack.clone());
            itemStack.setAmount(0);
            remove();
            return true;
        }
        itemStack.setAmount(0);
        playerData.getPlayer().setHealth(0.0);
        return false;
    }

    public void remove() {
        textDisplay.remove();
//        if (itemDisplay != null) itemDisplay.remove(Entity.RemovalReason.DISCARDED);
        TrablesBlockGUI.removeGUI(this);
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        return false;
    }



}
