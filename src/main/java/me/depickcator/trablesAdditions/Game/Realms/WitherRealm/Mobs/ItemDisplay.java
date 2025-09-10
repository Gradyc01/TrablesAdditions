package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import me.depickcator.trablesAdditions.Util.PlayerUtil;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemDisplay extends Display.ItemDisplay {
    public ItemDisplay(Location location, ItemStack itemStack) {
        super(EntityType.ITEM_DISPLAY, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        setItemStack(CraftItemStack.asNMSCopy(itemStack));
        setCustomName(CraftItemStack.asNMSCopy(itemStack).getDisplayName());
        setCustomNameVisible(true);
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
            Player player = this.level().getNearestPlayer(this, 1.5);
            if (player == null) return;
            PlayerUtil.giveItem((org.bukkit.entity.Player) player.getBukkitLivingEntity(), this.getItemStack().asBukkitCopy());
            this.remove(RemovalReason.KILLED);
        } catch (Exception e) {
            this.remove(RemovalReason.DISCARDED);
        }
    }
}
