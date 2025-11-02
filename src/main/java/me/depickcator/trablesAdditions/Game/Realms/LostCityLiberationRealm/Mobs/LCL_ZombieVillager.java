package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class LCL_ZombieVillager extends ZombieVillager implements RealmNMSMob {
    public LCL_ZombieVillager(Location location, Random random) {
        super(EntityType.ZOMBIE_VILLAGER, ((CraftWorld) location.getWorld()).getHandle());
        NMSMobUtil.setAndSpawn(this, location);
        this.targetSelector.removeAllGoals(goal -> true);
        this.goalSelector.removeAllGoals(goal -> true);
        super.goalSelector.addGoal(0, new FloatGoal(this));
        super.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.0F, false));
        super.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 4.0F, 1.0F));
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Mob.class)));
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        giveAttributes();
        this.equipment.set(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(getWeapon(random)));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.setPersistenceRequired(true);
        NMSMobUtil.generateRandomArmor(this.equipment, random);
    }

    private void giveAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(9);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.33F);
        this.getAttribute(Attributes.ARMOR).setBaseValue(15.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0F);
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(25.0F);
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0F);
        this.setShouldBurnInDay(false);
        this.setPersistenceRequired(true);
    }

    private ItemStack getWeapon(Random random) {
        float r = random.nextFloat();
        ItemStack item = null;
        if (r < 0.01) {
            item = generateWeapon(Material.NETHERITE_SHOVEL, 1);
        } else if (r < 0.05F) {
            item = generateWeapon(Material.DIAMOND_SHOVEL, 1);
        } else if (r < 0.15F) {
            item = generateWeapon(Material.IRON_SHOVEL, 1);
        } else if (r < 0.30F) {
            item = generateWeapon(Material.GOLDEN_SHOVEL, 1);
        } else if (r < 0.50F) {
            item = generateWeapon(Material.STONE_SHOVEL, 1);
        } else if (r < 0.70F) {
            item = generateWeapon(Material.COPPER_SHOVEL, 1);
        } else {
            item = generateWeapon(Material.WOODEN_SHOVEL, 1);
        }
        return item;
    }

    private ItemStack generateWeapon(Material material, int sharpnessLevel) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta =  item.getItemMeta();
        meta.addEnchant(Enchantment.SHARPNESS, sharpnessLevel, true);
        meta.setEnchantmentGlintOverride(false);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    protected void dropFromLootTable(ServerLevel level, DamageSource damageSource, boolean playerKill) {
//        NMSMobUtil.attemptToDropItemStack(ZombieHeart.getInstance().getResult(),  damageSource,this, 0.005);
    }

    @Override
    public void startConverting(@Nullable UUID conversionStarter, int villagerConversionTime, boolean broadcastEntityEvent) {}

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
        return Component.literal("Prisoner").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.WHITE.value())));
    }
}
