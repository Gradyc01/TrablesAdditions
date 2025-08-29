package me.depickcator.trablesAdditions.Game.Effects;

import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.List;

public class GolemLaunch {
    private Player player;
    private final boolean isAPlayer;
    private final LivingEntity golem;
    private final LivingEntity target;
    public GolemLaunch(LivingEntity target, LivingEntity golem) {
        this.isAPlayer = target instanceof Player;
        if (isAPlayer) this.player = (Player) target;
        this.target = target;
        this.golem = golem;
        start();
    }
    public GolemLaunch(Player target, LivingEntity golem) {
        this(golem, target);
    }

    private void start() {
        target.setVelocity(new Vector(0, 1.2, 0));
        if (isAPlayer) {
            player.playSound(SoundUtil.makeSound(Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1f, 1.0f),
                    net.kyori.adventure.sound.Sound.Emitter.self());
        }
        slam();
    }

    private void slam() {
        new BukkitRunnable() {
            @Override
            public void run() {
                double d = golem.getX() - target.getX() ;
                double d1 = (golem.getY() + 2) - target.getY() ;
                double d2 = golem.getZ() - target.getZ();
                target.setVelocity(new Vector(d, d1, d2).normalize().multiply(new Vector(1.0, 1.1, 1.0)));
                target.damage(0.1);
//                target.setHealth(player.getHealth() * 0.7);
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0, false, true));
                if (isAPlayer) {
                    player.playSound(SoundUtil.makeSound(Sound.BLOCK_ANVIL_LAND, 1f, 1.0f),
                            net.kyori.adventure.sound.Sound.Emitter.self());
                    player.playSound(SoundUtil.makeSound(Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.0f),
                            net.kyori.adventure.sound.Sound.Emitter.self());
                    player.playSound(SoundUtil.makeSound(Sound.BLOCK_ANVIL_BREAK, 1f, 1.0f),
                            net.kyori.adventure.sound.Sound.Emitter.self());
                }
            }
        }.runTaskLater(TrablesAdditions.getInstance(), 20);
    }
}
