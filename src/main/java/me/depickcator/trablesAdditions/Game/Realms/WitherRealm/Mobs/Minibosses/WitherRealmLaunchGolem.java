package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Minibosses;

import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmFireball;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WitherRealmLaunchGolem extends WitherRealmGolem implements RangedAttackMob {
    private int attackTimer;
    private int rangedAttackTime;

    public WitherRealmLaunchGolem(Location location, Random random) {
        super(location, random);
        rangedAttackTime = 120;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            if (attackTimer == rangedAttackTime) {
                rangeAttack(this.getTarget());
                rangedAttackTime = random.nextInt(80, 220);
            }
            if (attackTimer++ >= 250) attackTimer = 0;
        }

    }

    private void rangeAttack(LivingEntity entity) {
        new BukkitRunnable() {
            int iter = 0;
                @Override
                public void run() {
                    if (iter++ >= 2) cancel();
                    performRangedAttack(entity, 1.0f);
                }
            }.runTaskTimer(TrablesAdditions.getInstance(), 0, 5);
    }

    @Override
    public Component getMobName() {
        return Component.literal("Iron Disciple").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.WHITE.value())));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float v) {
//        this.getTargetEntity()
        TextUtil.debugText("Range", "Fire");
        if (target == null) return; //Intellij is wrong
        double d = target.getX() - this.getX();
        double d1 = (target.getY(0.1)) - (this.getY() + 2);
        double d2 = target.getZ() - this.getZ();

        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            Vec3 direction = (new Vec3(d, d1, d2)).normalize().multiply(2.0, 2.3, 2.0);
            WitherRealmFireball fireball = new WitherRealmFireball(level, this, direction, 2);
            fireball.setOwner(this);
            fireball.setItem(CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_BLOCK)));
            fireball.setPos(this.getX() + direction.x * 1.5, this.getY(0.5) + 0.5, this.getZ() + direction.z * 1.5);
            fireball.setRemainingFireTicks(0);
            serverLevel.addFreshEntity(fireball);
        }
    }
}
