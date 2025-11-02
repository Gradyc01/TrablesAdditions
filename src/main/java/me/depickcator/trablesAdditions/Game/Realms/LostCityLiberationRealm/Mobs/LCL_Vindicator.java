package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs;

import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ZombieHeart;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class LCL_Vindicator extends Vindicator implements RealmNMSMob {
    public LCL_Vindicator(Location location, Random random) {
        super(EntityType.VINDICATOR, ((CraftWorld) location.getWorld()).getHandle());
        NMSMobUtil.setAndSpawn(this, location);
        this.targetSelector.removeAllGoals(goal -> true);
        this.goalSelector.removeAllGoals(goal -> true);
        super.goalSelector.addGoal(0, new FloatGoal(this));
        super.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.0F, false));
        super.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F));
        super.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Mob.class)));
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        double speed = this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed - (random.nextFloat()) * (speed/3));
        this.getAttribute(Attributes.ARMOR).setBaseValue(10.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0F);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(35);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
        this.setPersistenceRequired(true);
        this.equipment.set(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(getWeapon(random)));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    private ItemStack getWeapon(Random random) {
        float r = random.nextFloat();
        ItemStack item = null;
        if (r < 0.01) {
            item = generateWeapon(Material.NETHERITE_AXE, 4);
        } else if (r < 0.05F) {
            item = generateWeapon(Material.DIAMOND_AXE, 3);
        } else if (r < 0.15F) {
            item = generateWeapon(Material.IRON_AXE, 3);
        } else if (r < 0.30F) {
            item = generateWeapon(Material.GOLDEN_AXE, 2);
        } else if (r < 0.50F) {
            item = generateWeapon(Material.STONE_AXE, 2);
        } else if (r < 0.70F) {
            item = generateWeapon(Material.COPPER_AXE, 1);
        } else {
            item = generateWeapon(Material.WOODEN_AXE, 1);
        }
        return item;
    }

    private ItemStack generateWeapon(Material material, int sharpnessLevel) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta =  item.getItemMeta();
        if (sharpnessLevel > 0) meta.addEnchant(Enchantment.SHARPNESS, sharpnessLevel, true);
        meta.setEnchantmentGlintOverride(false);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    protected void dropFromLootTable(ServerLevel level, DamageSource damageSource, boolean playerKill) {
//        NMSMobUtil.attemptToDropItemStack(ZombieHeart.getInstance().getResult(),  damageSource,this, 0.005);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        boolean bool = super.hurtServer(level, damageSource, amount);
        this.setCustomName(NMSMobUtil.generateHealthText(getMobName(), this));
        return bool;
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel level, @NotNull Entity source) {
        return super.doHurtTarget(level, source);
    }

    @Override
    public Component getMobName() {
        return Component.literal("Lost City Protector").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.WHITE.value())));
    }
}
