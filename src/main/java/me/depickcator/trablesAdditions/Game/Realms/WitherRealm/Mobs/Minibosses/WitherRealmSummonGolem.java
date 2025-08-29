package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Minibosses;

import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WitherRealmSummonGolem extends WitherRealmGolem {
    private int attackTimer;
    private final int summonTime;

    public WitherRealmSummonGolem(Location location, Random random) {
        super(location, random);
        summonTime = 300;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            if (attackTimer == summonTime && getTarget() != null) {
                spawnMiniGolems();
            }
            if (attackTimer++ >= 300) attackTimer = 0;
        }
    }

    private void spawnMiniGolems() {
        AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        IronGolem golem = this;
        attribute.setBaseValue(0);
        new BukkitRunnable() {
            int iter = 20;
            @Override
            public void run() {
                if (iter-- <= 0) {
                    attribute.setBaseValue(0.3);
                    cancel();
                }
                new WitherRealmMiniGolem(golem.getBukkitLivingEntity().getEyeLocation(), random);
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 20, 1);
    }

    @Override
    public Component getMobName() {
        return Component.literal("Iron Disciple").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.WHITE.value())));
    }
}
