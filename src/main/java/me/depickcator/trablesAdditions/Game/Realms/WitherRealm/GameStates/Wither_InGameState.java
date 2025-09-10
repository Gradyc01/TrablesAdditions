package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class Wither_InGameState extends WitherRealmState{

    public Wither_InGameState(WitherRealm realm) {
        super(realm);
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        TextUtil.debugText("onEntityExplode");
        if (event.getEntity() instanceof TNTPrimed) {
            List<Block> blocks = event.blockList();
            boolean noDoorTriggered = true;
            for (Block block : new ArrayList<>(blocks)) {
                if (!block.hasMetadata("PLACED")) blocks.remove(block);
                if (block.hasMetadata(WitherRealm.WITHER_REALM_DUNGEON_DOOR_KEY) && noDoorTriggered) {
                    noDoorTriggered = false;
                    getRealm().triggerDoor(block.getMetadata(WitherRealm.WITHER_REALM_DUNGEON_DOOR_KEY).getFirst().asString());
                };
            }
        }
        else {
            super.onEntityExplode(event);
        }
    }

    @Override
    public List<Component> getObjectiveName() {
        return List.of(
                TextUtil.makeText("Defeat Krivon's ", TextUtil.YELLOW),
                TextUtil.makeText("Disciples " + getRealm().getDisciplesRemaining() + " Left", TextUtil.YELLOW)
        );
    }

    @Override
    public String getStateName() {
        return "In Game";
    }
}
