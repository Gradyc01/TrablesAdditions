package me.depickcator.trablesAdditions.Game.Effects;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerStates.DefaultState;
import me.depickcator.trablesAdditions.Game.Player.PlayerStates.TrueSightState;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Entities.ItemDisplay;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.alchemy.Potion;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class LCLTrueSight {
    private final Player player;
    private final PotionEffect effect;
    private final int timer;
    private final RealmController controller;
    private final PlayerData playerData;
    private BukkitTask task;
    public LCLTrueSight(Player player, RealmController controller) {
        this.player = player;
        this.playerData = PlayerUtil.getPlayerData(player);
        this.controller = controller;
        PlayerUtil.getPlayerData(player).setPlayerState(new TrueSightState());
        effect = new PotionEffect(PotionEffectType.DARKNESS, 2 * 20, 0, true, false);
        timer = 90;
    }

    public void start() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 5 * 20, 127, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0, true, false));
        loop();
    }

    public void loop() {
        task = new BukkitRunnable() {
            int ticks = timer * 20;
            @Override
            public void run() {
                player.addPotionEffect(effect);
                if (ticks % (timer/10 * 20) == 0) displayExpiration(timer - (ticks/20));
                if (ticks % 20 == 0) player.playSound(SoundUtil.makeSound(Sound.UI_BUTTON_CLICK, 1F, 2F));
                if (player.getGameMode() != GameMode.SURVIVAL || controller.isRealmStopped() || !player.isOnline()
                        || !(playerData.getPlayerState() instanceof TrueSightState)) {
                    cancel();
                    stop();
                }
                if (--ticks <= 0) {
                    killPlayer();
                    cancel();
                }
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 1);
    }

    private void displayExpiration(int timePassed) {
        player.showTitle(TextUtil.makeTitle(TextUtil.makeText(""), displayBar((double) timePassed /timer),
                0, 1.5, 0.5));
        player.playSound(SoundUtil.makeSound(Sound.BLOCK_NOTE_BLOCK_BELL, 10F, (float) timePassed * 2 /timer));
    }

    private Component displayBar(double percentage) {
        Component text = TextUtil.makeText("[", TextUtil.WHITE);
        Component endText = TextUtil.makeText("]", TextUtil.WHITE);
        Component red = TextUtil.makeText(":", TextUtil.DARK_RED);
        Component green = TextUtil.makeText(":", TextUtil.GREEN);
        int score = (int) Math.round(25 * percentage);

        for (int i = 0; i < 25; i++) {
            if (i < score) {
                text = text.append(red);
            } else {
                text = text.append(green);
            }
        }
        return text.append(endText);
    }

    private void killPlayer() {
        player.setHealth(0.0);
        player.playSound(SoundUtil.makeSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10F, 2));
        stop();
    }

    public void cleanse() {
        player.playSound(SoundUtil.makeSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10F, 2));
        player.sendMessage(TextUtil.makeText("You have been FREED", TextUtil.WHITE, true, false));
        stop();
    }

    private void stop() {
        task.cancel();
        PlayerUtil.getPlayerData(player).returnToPreviousState();
    }


}
