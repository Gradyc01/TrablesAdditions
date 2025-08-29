package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Spider;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Spider;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

import java.util.Random;

public class WitherRealmSpider extends Spider implements RealmNMSMob {
    private final Random random;
    private final Component name;
    public WitherRealmSpider(Location location, Random random) {
        super(EntityType.SPIDER, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        this.random = random;
        name = getMobName();
        giveEffects();
    }

    private void giveEffects() {
        double num = random.nextDouble();
        Holder<MobEffect> effect = null;
        if (num <= 0.05) effect = MobEffects.INVISIBILITY;
        else if (num <= 0.15) effect = MobEffects.STRENGTH;
        else if (num <= 0.35) effect = MobEffects.FIRE_RESISTANCE;
        else if (num <= 0.60) effect = MobEffects.SPEED;
        if (effect != null) this.addEffect(new MobEffectInstance(effect, -1, 0, false, true));
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        boolean bool = super.hurtServer(level, damageSource, amount);
        this.setCustomName(NMSMobUtil.generateHealthText(name, this));
        return bool;
    }

    @Override
    public Component getMobName() {
        return Component.literal("Dungeon Spider").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.GREEN.value())));
    }
}
