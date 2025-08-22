package me.depickcator.trablesAdditions.Game.Effects;

import me.depickcator.trablesAdditions.Game.Effects.Interfaces.Floodable;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RealmOpeningAnimation implements Floodable {
    private final Location centerPoint;
    private final ArmorStand armorStand;
    private final RealmController realmController;
    private final FloodBlocks floodBlocks;

    public RealmOpeningAnimation(Location centerPoint, RealmController realmController) {
        this.centerPoint = centerPoint;
        this.realmController = realmController;
        armorStand = initArmorstand();
        floodBlocks = new FloodBlocks(centerPoint.clone().add(0, -1, 0), 1.20, this);
        start();
    }

    private void start() {
        int ticks = 59 * 20;
        Random random = new Random();
        floodBlocks.start(random);
        new BukkitRunnable() {
            int timePassed = 0;
            @Override
            public void run() {
                if (timePassed % 100 == 0) {
                    spinParticle(0); spinParticle(Math.PI / 2);
                    spinParticle(Math.PI); spinParticle( 3 * Math.PI / 2);
                    spinParticle(Math.PI /4);spinParticle( 3 * Math.PI / 4);
                    spinParticle(5 * Math.PI / 4);spinParticle( 7 * Math.PI / 4);
                    floodBlocks.flood(random);
                    centerPoint.getWorld().playSound(centerPoint, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1F, 0);
                }

                if (timePassed % 20 == 0) {
                    armorStand.customName(TextUtil.makeText("Realm Opens in: ", TextUtil.GOLD)
                            .append(TextUtil.makeText(TextUtil.formatTime(ticks / 20 - timePassed / 20), TextUtil.AQUA)));
                    TextUtil.debugText("RealmAnimation", timePassed / 20 + " Seconds Passed" );
                }
                if (timePassed++ >= ticks) {
                    armorStand.remove();
                    realmController.openPortal();
                    cancel();
                }
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 1);
    }

    private ArmorStand initArmorstand() {
        ArmorStand entity = (ArmorStand) centerPoint.getWorld().spawnEntity(centerPoint.clone().add(0, 1.5, 0), EntityType.ARMOR_STAND);
        entity.setMarker(true);
        entity.setCustomNameVisible(true);
        entity.setVisible(false);
        return entity;
    }

    private void spinParticle(double initialTime) {
//        int ticks = 10 * 20;
        new BukkitRunnable() {
            double t = initialTime;
            double y = 0;
            @Override
            public void run() {

                t += 0.05;
                y += 0.03;
                double x = 7 * Math.cos(t);
                double z = 7 * Math.sin(t);
                Location loc = centerPoint.clone().add(x, y, z);
//                loc.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0);
                loc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 1, 0, 0, 0, 0);
//                TextUtil.debugText("RealmAnimation", "Spawned particle at (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
                if (t >= Math.PI * 4 + initialTime) cancel();
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 1);

    }


    @Override
    public Block changeBlock(Block block, Random r, FloodBlocks floodBlocks) {
        block.setType(floodBlocks.chooseNextMaterial(r));
        return block;
    }

    @Override
    public Map<Material, Integer> getUnFloodables() {
        return Map.of(
                Material.SOUL_SAND, 20,
                Material.LAVA, 5,
                Material.NETHERRACK, 75,
                Material.AIR, 0);
    }

    @Override
    public boolean isUnFloodable(Block b) {
        return getUnFloodables().containsKey(b.getType());
    }

    @Override
    public double getNewSuccessRate(double oldSuccessRate, Random r) {
        return oldSuccessRate - r.nextDouble(0.1, 0.20);
    }
}