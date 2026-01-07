package net.justonedev.mc;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Objects;

public class NetherEvents implements Listener {

    @EventHandler
    public void onPortalCreated(PortalCreateEvent e) {
        if (e.getReason() == PortalCreateEvent.CreateReason.NETHER_PAIR    // allow the creation of a pair
            || e.getReason() == PortalCreateEvent.CreateReason.END_PLATFORM) return;

        if (!DisableEnd.CURRENT_NETHER_DISABLED) return;
        if (e.getEntity() instanceof Player && isBypass((Player) e.getEntity())) return;
        if (e.getWorld().getEnvironment() == World.Environment.NETHER && DisableEnd.CURRENT_ALLOW_NETHER_EXIT) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityEnterPortal(EntityPortalEnterEvent e) {
        // todo no
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        if (!DisableEnd.CURRENT_NETHER_DISABLED) return;
        if (!DisableEnd.CURRENT_DENY_NETHER_TELEPORT) return;
        Player p = continueEvent(e);
        if (p == null) return;
        if (!DisableEnd.CURRENT_MSG_DENY_NETHER_TP.isBlank()) p.sendMessage(DisableEnd.CURRENT_MSG_DENY_NETHER_TP);
        World defaultWorld = Bukkit.getWorlds().getFirst();
        if (defaultWorld == null && p.getRespawnLocation() == null) return;
        p.teleport(p.getRespawnLocation() == null ? Objects.requireNonNull(defaultWorld).getSpawnLocation() : p.getRespawnLocation());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!DisableEnd.CURRENT_NETHER_DISABLED) return;

        if (isNotInNether(e)) {
            Block block = e.getPlayer().getLocation().getBlock();
            if (block.getType() == Material.NETHER_PORTAL) {
                Block current;
                do {
                    current = block.getRelative(BlockFace.DOWN);
                } while (current.getType() == Material.NETHER_PORTAL);
                if (current.getType() != Material.OBSIDIAN) return;

                // Destroy portal
                current.setType(Material.AIR);
                current.setType(Material.OBSIDIAN);
            }
            return;
        }

        if (!DisableEnd.CURRENT_KICK_PLAYERS) return;
        Player p = continueEvent(e);
        if (p == null) return;
        if (!DisableEnd.CURRENT_MSG_KICK_FROM_NETHER.isBlank()) p.sendMessage(DisableEnd.CURRENT_MSG_KICK_FROM_NETHER);
        World defaultWorld = Bukkit.getWorlds().getFirst();
        if (defaultWorld == null && p.getRespawnLocation() == null) return;
        p.teleport(p.getRespawnLocation() == null ? Objects.requireNonNull(defaultWorld).getSpawnLocation() : p.getRespawnLocation());
    }

    private static boolean isNotInNether(PlayerEvent e) {
        return !e.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER);
    }

    private static Player continueEvent(PlayerEvent e) {
        if (isBypass(e.getPlayer())) return null;
        if (isNotInNether(e)) return null;
        return e.getPlayer();
    }

    private static boolean isBypass(Player p) {
        return DisableEnd.CURRENT_BYPASS_CREATIVE_ADMINS && p.getGameMode() == GameMode.CREATIVE && p.isOp();
    }

}
