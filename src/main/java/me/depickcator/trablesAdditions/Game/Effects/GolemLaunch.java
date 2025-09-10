package me.depickcator.trablesAdditions.Game.Effects;

import me.depickcator.trablesAdditions.Game.Effects.Interfaces.ImmuneToEffects;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.bukkit.*;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
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
    private final Location centerPoint;
    public GolemLaunch(LivingEntity target, LivingEntity golem, Location location) {
        this.isAPlayer = target instanceof Player;
        if (isAPlayer) this.player = (Player) target;
        this.centerPoint = location;
        this.target = target;
        this.golem = golem;
        if (((CraftLivingEntity) target).getHandle() instanceof ImmuneToEffects) return;
        start();
    }
    public GolemLaunch(LivingEntity target, LivingEntity golem) {
        this(target, golem, golem.getLocation());
    }

    private void start() {
        target.setVelocity(new Vector(0, 1.2, 0));
//        if (isAPlayer) {
//            player.playSound(SoundUtil.makeSound(Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1f, 1.0f),
//                    net.kyori.adventure.sound.Sound.Emitter.self());
//        }
        centerPoint.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, 1, 0, 1, 10, Material.STONE_BRICKS.createBlockData());
        centerPoint.getWorld().playSound(centerPoint, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 2.0f, 1.0f);
        slam();
    }

    private void slam() {
        new BukkitRunnable() {
            @Override
            public void run() {
                double d = centerPoint.getX() - target.getX() ;
                double d1 = (centerPoint.getY() + 2) - target.getY() ;
                double d2 = centerPoint.getZ() - target.getZ();
                target.setVelocity(new Vector(d, d1, d2).normalize().multiply(new Vector(1.0, 1.1, 1.0)));

                if (!isAPlayer && !target.isDead()) target.setHealth(Double.max(0.1, target.getHealth() * 0.7));
                target.damage(0.1, golem);
//                target.setHealth(player.getHealth() * 0.7);
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0, false, true));
//                if (isAPlayer) {
//                    player.playSound(SoundUtil.makeSound(Sound.BLOCK_ANVIL_LAND, 1f, 1.0f),
//                            net.kyori.adventure.sound.Sound.Emitter.self());
//                    player.playSound(SoundUtil.makeSound(Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.0f),
//                            net.kyori.adventure.sound.Sound.Emitter.self());
//                    player.playSound(SoundUtil.makeSound(Sound.BLOCK_ANVIL_BREAK, 1f, 1.0f),
//                            net.kyori.adventure.sound.Sound.Emitter.self());
//                }
                centerPoint.getWorld().playSound(centerPoint, Sound.BLOCK_ANVIL_LAND, 1.5f, 1.0f);
                centerPoint.getWorld().playSound(centerPoint, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 0.0f);
                centerPoint.getWorld().playSound(centerPoint, Sound.BLOCK_ANVIL_BREAK, 2.0f, 1.0f);
            }
        }.runTaskLater(TrablesAdditions.getInstance(), 20);
    }
}
