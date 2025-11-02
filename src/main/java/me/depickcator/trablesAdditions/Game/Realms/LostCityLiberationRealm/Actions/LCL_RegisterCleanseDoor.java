package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import io.papermc.paper.math.Rotation;
import me.depickcator.trablesAdditions.Game.Effects.LCLTrueSight;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerStates.TrueSightState;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class LCL_RegisterCleanseDoor extends RealmActions {
    private final List<Block> blocks;
    public LCL_RegisterCleanseDoor(String meshName, RealmController controller) {
        super(meshName, controller);
        blocks = new ArrayList<>();
    }

    public Set<Block> registerCoordinates() {
        World world = controller.getWorld();
        Set<Block> blocks = new HashSet<>();
        try {
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, world);
            for (Pair<Location, Integer> locations : mesh.getAllLocationsWeighted()) {
                Block block = locations.getLeft().getBlock();
                if (locations.getRight() == 1) blocks.add(block);
                else this.blocks.add(block);
            }
        } catch (IOException | NoSuchElementException e) {
            TextUtil.debugText("Action Break Door", e.getMessage());
            controller.stopRealm();
        }
        return blocks;
    }

    @Override
    public boolean start() {
        return start(PlayerUtil.getPlayerData(controller.getPlayingPlayers().getFirst()));
    }

    public boolean start(PlayerData playerData) {
        if (blocks.size() < 2) {
            controller.stopRealm();
            return false;
        }
        Player player = playerData.getPlayer();
        if (!(playerData.getPlayerState() instanceof TrueSightState)) {
            TextUtil.errorMessage(player, "You do not have true sight and can not enter!");
            return false;
        }
        Location loc = player.getLocation();
        double d1 = loc.distance(blocks.getFirst().getLocation().toCenterLocation());
        double d2 = loc.distance(blocks.getLast().getLocation().toCenterLocation());
        Rotation rotation = loc.getRotation();
        if (d1 > d2) player.teleport(blocks.getFirst().getLocation().toCenterLocation());
        else player.teleport(blocks.getLast().getLocation().toCenterLocation());
        player.setRotation(rotation.yaw(), rotation.pitch());
        player.playSound(SoundUtil.makeSound(Sound.BLOCK_IRON_DOOR_OPEN, 10F, 1F));
        return true;
    }
}
