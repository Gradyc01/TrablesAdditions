package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Skeletons;

import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CupidEssence;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmFireball;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class WitherRealmRangerSkeleton extends Stray implements RealmNMSMob {
    private final Component name;
    public WitherRealmRangerSkeleton(Location location) {
        super(EntityType.STRAY, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        name = getMobName();
        giveAttributes();
    }

    private void giveAttributes() {
        this.equipment.set(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(initBow()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.equipment.set(EquipmentSlot.HEAD, initArmor(Material.CHAINMAIL_HELMET, "Helmet"));
        this.equipment.set(EquipmentSlot.CHEST, initArmor(Material.CHAINMAIL_CHESTPLATE, "Chestplate"));
        this.equipment.set(EquipmentSlot.LEGS, initArmor(Material.CHAINMAIL_LEGGINGS, "Leggings"));
        this.equipment.set(EquipmentSlot.FEET, initArmor(Material.CHAINMAIL_BOOTS, "Boots"));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(50.0F);
        this.setShouldBurnInDay(false);
        this.setPersistenceRequired(true);
        super.targetSelector.removeAllGoals(goal -> true);
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Skeleton.class, Stray.class, Bogged.class, Drowned.class)));
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.reassessWeaponGoal();
//        this.equipment(EquipmentSlot.Mob.getEquipmentForSlot(EquipmentSlot.HEAD, random.nextInt(4));
    }

    private net.minecraft.world.item.ItemStack initArmor(Material material, String name) {
        ItemStack item = new ItemStack(material);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.customName(TextUtil.makeText("Ranger's " + name, TextUtil.YELLOW));
        meta.addEnchant(Enchantment.PROJECTILE_PROTECTION, 3, true);
        meta.addEnchant(Enchantment.UNBREAKING, 3, true);
        meta.setMaxDamage(512);
        item.setItemMeta(meta);
        return CraftItemStack.asNMSCopy(item);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        boolean bool = super.hurtServer(level, damageSource, amount);
        this.setCustomName(NMSMobUtil.generateHealthText(name, this));
        return bool;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        NMSMobUtil.attemptToDropItemStack(CupidEssence.getInstance().getResult(), damageSource, this, 0.1);
    }

    private ItemStack initBow() {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.addEnchant(Enchantment.POWER, 10, true);
        bow.setItemMeta(meta);
        return bow;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
//        super.performRangedAttack(target, distanceFactor);
        double d = target.getX() - this.getX();
        double d1 = target.getY(0.11111111111) - (this.getY() + 2);
        double d2 = target.getZ() - this.getZ();

        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
//            TextUtil.debugText("Ranger Skeleton", distanceFactor + "");
            Vec3 direction = (new Vec3(d, d1, d2)).normalize().multiply(1.0, 1.00, 1.0);

            WitherRealmFireball fireball = new WitherRealmFireball(
                    level,
                    this,
                    direction,
                    9
            );

            fireball.setOwner(this);

            fireball.setPos(
                    this.getX() + direction.x * 1.5,
                    this.getY(0.5) + 0.5,
                    this.getZ() + direction.z * 1.5
            );
            fireball.setRemainingFireTicks(10);

            serverLevel.addFreshEntity(fireball);
        }

        this.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }



    @Override
    public Component getMobName() {
        return Component.literal("Ranger Skeleton").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.GREEN.value())));
    }
}
