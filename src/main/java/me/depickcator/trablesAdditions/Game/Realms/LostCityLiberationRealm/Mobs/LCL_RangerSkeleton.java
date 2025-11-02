package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs;

import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CupidEssence;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Skeletons.WitherRealmRangerSkeleton;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Location;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;

public class LCL_RangerSkeleton extends WitherRealmRangerSkeleton {
    private final ItemStack item;

    public LCL_RangerSkeleton(Location location, ItemStack item) {
        super(location, true);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0F);
        this.getAttribute(Attributes.ARMOR).setBaseValue(10.0F);
        this.setHealth(this.getMaxHealth());
        this.item = item;
    }

    @Override
    public Component getMobName() {
        return Component.literal("Souless Ranger").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.YELLOW.value())));
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        NMSMobUtil.attemptToDropItemStack(item, damageSource, this, 1);
    }
}
