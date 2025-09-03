package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmEndCrystal;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockMenuGUI;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WitherRealmControlPanelGUI extends TrablesBlockMenuGUI {
    private int passKeys;
    private final ItemStack correctButton;
    private final ItemStack wrongButton;
    private final Random random;
    private final RealmController controller;
    private final List<Location> beamPoints;
    private final Location centerPoint;
    private final List<WitherRealmEndCrystal> crystals;

    public WitherRealmControlPanelGUI(Block block, RealmController controller, Location centerPoint, List<Location> beamPoints) {
        super(block, 6, TextUtil.makeText("Initiate Launch", TextUtil.AQUA), false);
        this.controller = controller;
        this.beamPoints = beamPoints;
        this.crystals = new ArrayList<>();
        this.centerPoint = centerPoint;
        passKeys = 0;
        correctButton = initCorrectButton();
        wrongButton = initWrongButton();
        random = new Random();
        resetPassKey();
        generateNextPassKey();
    }

    private void resetPassKey() {
        for (WitherRealmEndCrystal crystal : new ArrayList<>(crystals)) {
            crystal.ignite();
            crystals.remove(crystal);
        }
        for (Location beamPoint : beamPoints) {
            new WitherRealmEndCrystal(beamPoint, centerPoint, crystals);
        }
    }

    private void generateNextPassKey() {
        passKeys++;
        setBackground();
        for (int i = 0; i < passKeys; i++) {
            inventory.setItem(random.nextInt(0, 54), wrongButton);
        }
        inventory.setItem(random.nextInt(0, 54), correctButton);
    }

    private ItemStack initWrongButton() {
        return initExplainerItem(Material.GREEN_STAINED_GLASS_PANE, List.of(), TextUtil.makeText("❌", TextUtil.DARK_GREEN));
    }

    private ItemStack initCorrectButton() {
        return initExplainerItem(Material.GREEN_STAINED_GLASS_PANE, List.of(), TextUtil.makeText("✔", TextUtil.DARK_GREEN));
    }

    @Override
    public boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event) {
        if (event.getAction().isRightClick() && inventory.getViewers().isEmpty()) {
            open(playerData.getPlayer());
        }
        return true;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        event.setCancelled(true);
        Player p = playerData.getPlayer();
        ItemStack item = event.getCurrentItem();
        if (item.equals(correctButton)) {
            if (!crystals.isEmpty()) crystals.removeFirst().ignite();

            if (passKeys >= beamPoints.size()) {
                successfullyCompleted();
                SoundUtil.playHighPitchPling(p);
                return true;
            }
            p.playSound(SoundUtil.makeSound(Sound.BLOCK_NOTE_BLOCK_BELL, 10F, (float) (0.1 * passKeys)),
                    net.kyori.adventure.sound.Sound.Emitter.self());
        } else {
            passKeys = 0;
            resetPassKey();
            SoundUtil.playErrorSoundEffect(p);
        }
        generateNextPassKey();
        return true;
    }

    protected void successfullyCompleted() {
        inventory.close();
        controller.startBossFight();
        TrablesBlockGUI.removeGUI(this);
    }
}
