package me.depickcator.trablesAdditions.Game.Realms.SharedEntities.StartNPC;

import me.depickcator.trablesAdditions.Game.Mechanics.EntityInteractions;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.EntityInteractable;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StartingNPC implements EntityInteractable {
    private final RealmController controller;
    private final TrablesAdditions plugin;
    private final LivingEntity entity;
    private final Set<UUID> readiedPlayers;
    public StartingNPC(RealmController controller) {
        this.controller = controller;
        this.plugin = TrablesAdditions.getInstance();
        this.readiedPlayers = new HashSet<>();
        entity = initEntity();
        plugin.getEntityInteractions().addInteraction(this);
    }

    public boolean playerHasReadied(UUID uuid) {
        return readiedPlayers.contains(uuid);
    }

    public void addReadiedPlayer(PlayerData playerData) {
        readiedPlayers.add(playerData.getPlayer().getUniqueId());
        Player player = playerData.getPlayer();
        player.sendMessage(TextUtil.makeText("You have readied up!", TextUtil.GREEN));
        SoundUtil.playHighPitchPling(player);
        checkReadiedCount();
    }

    private void checkReadiedCount() {
        if (readiedPlayers.size() >= controller.getWorld().getPlayers().size()) {
            controller.startRealm();
            removeEntity();
        }
    }

    @Override
    public boolean interact(PlayerData playerData, PlayerInteractEntityEvent event) {
        new StartingNPCGUI(playerData, this);
        return true;
    }

    @Override
    public LivingEntity getEntity() {
        return entity;
    }

    private LivingEntity initEntity() {
        World world = controller.getWorld();
        Location spawnLocation = world.getSpawnLocation();
        try {
            LocationMesh mesh = controller.getReader().getLocationsMesh("starting_npc", world);
            spawnLocation = mesh.getAllLocations().getFirst();
        } catch (IOException ignored) {}
        LivingEntity e = (LivingEntity) world.spawnEntity(spawnLocation, EntityType.VILLAGER);
        e.setInvulnerable(true);
        e.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0);
        e.setSilent(true);
        e.customName(TextUtil.makeText("Realm Master", TextUtil.GOLD));
        e.setCustomNameVisible(true);
        return e;
    }

    @Override
    public void removeEntity() {
        plugin.getEntityInteractions().removeInteraction(entity);
        entity.remove();
    }
}
