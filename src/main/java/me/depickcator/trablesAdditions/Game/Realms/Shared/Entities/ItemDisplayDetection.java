package me.depickcator.trablesAdditions.Game.Realms.Shared.Entities;

import me.depickcator.trablesAdditions.Util.PlayerUtil;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemDisplayDetection extends ItemDisplay {
    public ItemDisplayDetection(Location location, ItemStack itemStack) {
        this(location, itemStack, location.getWorld().getPlayers());
    }

    public ItemDisplayDetection(Location location, ItemStack itemStack, List<org.bukkit.entity.Player> players) {
        super(location, itemStack, players);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            checkIfPlayerWalksIn();
        }
    }

    private void checkIfPlayerWalksIn() {
        try {
            Player player = this.level().findNearbyPlayer(this, 1.5, p -> playerUUIDs.contains(p.getUUID()));
            if (player == null) return;
            uponPlayerWalkIn((org.bukkit.entity.Player)  player.getBukkitLivingEntity());
        } catch (Exception e) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    protected void uponPlayerWalkIn(org.bukkit.entity.Player player) {
        PlayerUtil.giveItem(player, this.getItemStack().asBukkitCopy());
        this.remove(RemovalReason.KILLED);
    }
}
