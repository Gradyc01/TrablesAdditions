package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.Random;

public class LCL_RegisterSpawns extends LCL_SpawnIn{
    private final Collection<LivingEntity> livingEntities;
    public LCL_RegisterSpawns(String meshName, RealmController controller, Collection<LivingEntity> entities) {
        super(meshName, controller);
        this.livingEntities = entities;
    }

    @Override
    protected LivingEntity getMob(int mobType, Location location, Random random) {
        LivingEntity entity = super.getMob(mobType, location, random);
        livingEntities.add(entity);
        return entity;
    }
}
