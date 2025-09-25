package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import net.kyori.adventure.text.Component;

import java.util.List;

public interface EntranceKey {
    boolean canPurchase(PlayerData pD);
    List<Component> description();
    List<Component> purchaseRequirements();
    Component teamSize();
    List<Component> requiredItems();
}
