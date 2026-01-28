package net.justonedev.mc;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.List;

public class NetherEvents implements Listener {

    @EventHandler
    public void onPortalCreated(PortalCreateEvent e) {
        if (e.getReason() == PortalCreateEvent.CreateReason.NETHER_PAIR    // allow the creation of a pair
            || e.getReason() == PortalCreateEvent.CreateReason.END_PLATFORM) return;

        if (!DisableEnd.CURRENT_NETHER_DISABLED) return;
        if (e.getEntity() instanceof Player && isBypass(e.getEntity())) return;
        if (e.getWorld().getEnvironment() == World.Environment.NETHER && DisableEnd.CURRENT_ALLOW_NETHER_EXIT) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityEnterPortal(EntityPortalEnterEvent e) {
        handlePortalEntered(e.getLocation().getBlock(), e.getEntity());
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        if (!DisableEnd.CURRENT_NETHER_DISABLED) return;
        if (!DisableEnd.CURRENT_DENY_NETHER_TELEPORT) return;
        Player p = continueEvent(e);
        if (p == null) return;
        if (!DisableEnd.CURRENT_MSG_DENY_NETHER_TP.isBlank()) p.sendMessage(DisableEnd.CURRENT_MSG_DENY_NETHER_TP);
        Location teleportLocation = DisableEnd.getPlayerTeleportLocation(p);
        if (teleportLocation == null) return;
        p.teleport(teleportLocation);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!DisableEnd.CURRENT_NETHER_DISABLED) return;
        Block block = e.getPlayer().getLocation().getBlock();

        handlePortalEntered(block, e.getPlayer());
    }

    private static boolean isNotInNether(PlayerEvent e) {
        return isNotInNether(e.getPlayer().getWorld());
    }

    private static boolean isNotInNether(World w) {
        if (w == null) return true;
        return !w.getEnvironment().equals(World.Environment.NETHER);
    }

    private static Player continueEvent(PlayerEvent e) {
        if (isBypass(e.getPlayer())) return null;
        if (isNotInNether(e)) return null;
        return e.getPlayer();
    }

    private static Entity continueEvent(EntityEvent e) {
        if (isBypass(e.getEntity())) return null;
        if (isNotInNether(e.getEntity().getWorld())) return null;
        return e.getEntity();
    }

    private static boolean isBypass(Entity entity) {
        if (!(entity instanceof Player p)) return false;
        return DisableEnd.CURRENT_BYPASS_CREATIVE_ADMINS && p.getGameMode() == GameMode.CREATIVE && p.isOp();
    }

    private static void handlePortalEntered(Block portalBlock, Entity entity) {
        if (!DisableEnd.CURRENT_NETHER_DISABLED) return;

        if (isBypass(entity)) return;
        if (List.of(EntityType.ENDER_PEARL, EntityType.EYE_OF_ENDER).contains(entity.getType())) {
            entity.remove();
        }

        boolean notInNether = isNotInNether(portalBlock.getWorld());

        if ((notInNether || !DisableEnd.CURRENT_ALLOW_NETHER_EXIT) && portalBlock.getType() == Material.NETHER_PORTAL) {
            // Not in nether -> destroy portal
            breakPortal(portalBlock);
        } else if (!notInNether && DisableEnd.CURRENT_KICK_NETHER_PLAYERS) {
            // Nether is disabled, but player is in Nether
            if (!DisableEnd.CURRENT_MSG_KICK_FROM_NETHER.isBlank()) entity.sendMessage(DisableEnd.CURRENT_MSG_KICK_FROM_NETHER);

            World defaultWorld = DisableEnd.getDefaultWorld();
            if (defaultWorld == null && (!(entity instanceof Player) || ((Player) entity).getRespawnLocation() == null)) return;

            Location teleportLocation = entity instanceof Player
                    ? DisableEnd.getPlayerTeleportLocation((Player) entity)
                    : DisableEnd.getDefaultWorld().getSpawnLocation();
            if (teleportLocation == null) return;
            entity.teleport(teleportLocation);
        }
    }

    private static void breakPortal(Block portalBlock) {
        Block current = portalBlock;
        do {
            current = current.getRelative(BlockFace.DOWN);
        } while (current.getType() == Material.NETHER_PORTAL);
        if (current.getType() != Material.OBSIDIAN) return;

        // Destroy portal
        current.setType(Material.AIR);
        current.setType(Material.OBSIDIAN);
    }

}
