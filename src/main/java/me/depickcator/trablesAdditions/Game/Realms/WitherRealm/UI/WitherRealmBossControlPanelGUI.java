package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action.WitherRealm_BreakDoor;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates.Wither_BossState;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmEndCrystal;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockMenuGUI;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class WitherRealmBossControlPanelGUI extends TrablesBlockMenuGUI {
    private int passKeys;
    private final ItemStack correctButton;
    private final ItemStack wrongButton;
    private final Random random;
    private final RealmController controller;
    private final WitherRealmEndCrystal crystal;
    private final String rigMeshName;

    public WitherRealmBossControlPanelGUI(Block block, WitherRealmEndCrystal crystal, RealmController controller, String rigMeshName) {
        super(block, 6, TextUtil.makeText("Disable Shield", TextUtil.AQUA), true);
        this.passKeys = 0;
        correctButton = initCorrectButton();
        wrongButton = initWrongButton();
        random = new Random();
        this.controller = controller;
        this.rigMeshName = rigMeshName;
        this.crystal = crystal;
        resetPassKey();
        generateNextPassKey();
        if (getBossState() != null) getBossState().getBossFight().addPanel(this);
    }
//TODO: need to ignite once complete
    private void resetPassKey() {
        inventory.close();
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

    private Wither_BossState getBossState() {
        if (controller.getRealmState() instanceof Wither_BossState) {
            return (Wither_BossState) controller.getRealmState();
        } else {
            controller.stopRealm();
            TextUtil.debugText("Boss Control Panel", "Boss State is wrong");
            return null;
        }
    }

    @Override
    public boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event) {
        // if bossState is correct return true
//        TextUtil.debugText("Interacting with block. " + bossState.canOpenPanel());
//        TextUtil.debugText("Interacting with block " + inventory.getViewers().isEmpty());
        if (event.getAction().isRightClick() && getBossState() != null && getBossState().canOpenPanel() && inventory.getViewers().isEmpty()) {
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
            if (passKeys >= 1) {
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
        crystal.ignite();
        getBlock().setType(Material.AIR);
        new WitherRealm_BreakDoor(rigMeshName, controller).start();
        if (getBossState() != null) getBossState().getBossFight().removePanel(this);
        TrablesBlockGUI.removeGUI(this);
    }
}
