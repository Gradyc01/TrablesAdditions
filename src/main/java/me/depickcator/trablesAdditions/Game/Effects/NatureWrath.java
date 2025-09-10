package me.depickcator.trablesAdditions.Game.Effects;

import me.depickcator.trablesAdditions.Game.Effects.Interfaces.ImmuneToEffects;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class NatureWrath {
    private Player player;
    private final boolean isAPlayer;
    private final LivingEntity entity;
    private final int seconds;
    private final World world;
    public NatureWrath(LivingEntity entity, int seconds) {
        this.entity = entity;
        this.isAPlayer = entity instanceof Player;
        if (this.isAPlayer) {this.player = (Player) entity;}
        world = this.entity.getWorld();
        this.seconds = seconds;
        if (((CraftLivingEntity) entity).getHandle() instanceof ImmuneToEffects) return;
        start();
    }



    private void start() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, seconds * 20, 9, false, false));
        if (isAPlayer) {
            player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 2.0f);
            player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_RETURN, 100, 2);
        }
        loop();
    }

    private void loop() {
        Component text = TextUtil.makeText("SMITED", TextUtil.GRAY);
        DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).build();
        if (this.isAPlayer) {
            player.showTitle(TextUtil.makeTitle(text, 0, seconds - 2, 2));
        }
        new BukkitRunnable() {
            int timer = seconds;
            public void run() {
                if (entity.isDead() || (isAPlayer && player.getGameMode() != GameMode.SURVIVAL)) {
                    canceled();
                    cancel();
                    return;
                }
                if (timer <= 0) {
                    stop();
                    cancel();
                    return;
                }
                Location loc = entity.getLocation();
                if (isAPlayer) {
                    player.sendMessage(text);
                    player.playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 2.0f);
                }

                entity.setHealth(Double.max(0.1, entity.getHealth() - 2.5));
                world.strikeLightningEffect(loc);
                world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.0F);
                world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0F, 0.0F);
//                world.spawnEntity(player.getLocation(), EntityType.LIGHTNING_BOLT);
                entity.damage(1, source);

                timer--;
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 20);
    }

    private void stop() {

    }

    private void canceled() {
    }
}
