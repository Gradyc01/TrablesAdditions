package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealmBossFight;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockMenuGUI;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public abstract class WitherRealmControlPanelGUI extends TrablesBlockMenuGUI {
    protected int passKeys;
    protected final ItemStack correctButton;
    protected final ItemStack wrongButton;
    protected final Random random;
    public WitherRealmControlPanelGUI(Block block, Component name) {
        super(block, 6, name, true);
        this.passKeys = 0;
        correctButton = initCorrectButton();
        wrongButton = initWrongButton();
        random = new Random();
        generateNextPassKey();
    }

    private ItemStack initWrongButton() {
        return initExplainerItem(Material.GREEN_STAINED_GLASS_PANE, List.of(), TextUtil.makeText("❌", TextUtil.DARK_GREEN));
    }

    private ItemStack initCorrectButton() {
        return initExplainerItem(Material.GREEN_STAINED_GLASS_PANE, List.of(), TextUtil.makeText("✔", TextUtil.DARK_GREEN));
    }

    @Override
    public boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event) {
        TextUtil.debugText("Interacting with Block", " Is Right Click " + event.getAction().isRightClick());
        TextUtil.debugText("Interacting with Block", " Is Empty " + inventory.getViewers().isEmpty());
        if (event.getAction().isRightClick() && inventory.getViewers().isEmpty()) {
            TextUtil.debugText("Interacting with Block", " Opening");
            open(playerData.getPlayer());
            return true;
        }
        return true;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        event.setCancelled(true);
        Player p = playerData.getPlayer();
        ItemStack item = event.getCurrentItem();
        if (item.equals(correctButton)) {
            onClickCorrectButton(playerData);

            if (hasReachSuccessfulCompletion()) {
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

    protected abstract void onClickCorrectButton(PlayerData playerData);
    protected abstract boolean hasReachSuccessfulCompletion();
    protected abstract void resetPassKey();
    protected abstract void successfullyCompleted();

    private void generateNextPassKey() {
        passKeys++;
        setBackground();
        for (int i = 0; i < passKeys; i++) {
            inventory.setItem(random.nextInt(0, 54), wrongButton);
        }
        inventory.setItem(random.nextInt(0, 54), correctButton);
    }
}
