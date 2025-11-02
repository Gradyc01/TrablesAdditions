package me.depickcator.trablesAdditions.Game.Player.PlayerStates;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TrueSightState extends PlayerAbstractState {
    @Override
    public boolean onTeleport(PlayerData pD, PlayerTeleportEvent event) {
        return (event.getCause() != PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public String getStateName() {
        return "True Sight";
    }
}
