package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.List;

public class Wither_DisciplesGoneState extends Wither_InGameState {

    public Wither_DisciplesGoneState(WitherRealm realm) {
        super(realm);

    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            boolean noDoorTriggered = true;
            List<Block> blocks = event.blockList();
            for (Block block : new ArrayList<>(blocks)) {
                if (!block.hasMetadata("PLACED")) blocks.remove(block);
                if (block.hasMetadata(WitherRealm.WITHER_REALM_BLOOD_DOOR_KEY) && noDoorTriggered) {
                    noDoorTriggered = false;
                    getRealm().triggerDoor(block.getMetadata(WitherRealm.WITHER_REALM_BLOOD_DOOR_KEY).getFirst().asString());
                }
                if (block.hasMetadata(WitherRealm.WITHER_REALM_DUNGEON_DOOR_KEY) && noDoorTriggered) {
                    noDoorTriggered = false;
                    getRealm().triggerDoor(block.getMetadata(WitherRealm.WITHER_REALM_DUNGEON_DOOR_KEY).getFirst().asString());
                }
            }
        } else {
            super.onEntityExplode(event);
        }

    }



    @Override
    public List<Component> getObjectiveName() {
        return List.of(
                TextUtil.makeText("Enter Blood Room", TextUtil.YELLOW)
        );
    }

    @Override
    public String getStateName() {
        return "In Game: Disciples Gone";
    }
}
