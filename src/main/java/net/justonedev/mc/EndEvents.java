package net.justonedev.mc;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class EndEvents implements Listener {

    @EventHandler
    public void onPlaceEndereye(PlayerInteractEvent e) {
        if (!DisableEnd.CURRENT_END_DISABLED) return;
        if (DisableEnd.CURRENT_BYPASS_CREATIVE_ADMINS && e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().isOp()) return;
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem() == null) return;
        if (e.getItem().getType() != Material.ENDER_EYE) return;
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getType() != Material.END_PORTAL_FRAME) return;
        if (((EndPortalFrame) e.getClickedBlock().getBlockData()).hasEye()) return;

        Player p = e.getPlayer();

        if (DisableEnd.CURRENT_DO_DENY_SOUNDS) {
            p.playSound(p, Sound.ITEM_TOTEM_USE, 0.8f, 1);
        }

        // Valid ones: Particle.FLASH, Particle.INFESTED, Particle.POOF
        // Maybe: (Particle.EXPLOSION, Particle.EXPLOSION_EMITTER)

        if (DisableEnd.CURRENT_DO_PARTICLES) {
            Location loc = e.getClickedBlock().getLocation().add(0.5, 0.75, 0.5);
            trySpawnParticle(Particle.FLASH, loc, 65);
            trySpawnParticle(Particle.INFESTED, loc, 75);
            trySpawnParticle(Particle.POOF, loc, 25);
        }
        e.setCancelled(true);
    }
    private void trySpawnParticle(Particle particle, Location location, int count) {
        trySpawnParticle(particle, location, count, false);
    }
    private void trySpawnParticle(Particle particle, Location location, int count, boolean printStackTrace) {
        if (location.getWorld() == null) return;
        try {
            location.getWorld().spawnParticle(particle, location, count);
        } catch (IllegalArgumentException e) {
            if (printStackTrace) e.printStackTrace();
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        if (!DisableEnd.CURRENT_END_DISABLED) return;
        if (!DisableEnd.CURRENT_DENY_TELEPORT) return;
        Player p = continueEvent(e);
        if (p == null) return;
        if (DisableEnd.hasText(DisableEnd.CURRENT_MSG_DENY_WORLD_TP)) p.sendMessage(DisableEnd.CURRENT_MSG_DENY_WORLD_TP);
        Location teleportLocation = DisableEnd.getPlayerTeleportLocation(p);
        if (teleportLocation == null) return;
        p.teleport(teleportLocation);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!DisableEnd.CURRENT_END_DISABLED) return;
        if (!DisableEnd.CURRENT_KICK_PLAYERS) return;
        Player p = continueEvent(e);
        if (p == null) return;
        if (DisableEnd.hasText(DisableEnd.CURRENT_MSG_KICK_FROM_END)) p.sendMessage(DisableEnd.CURRENT_MSG_KICK_FROM_END);
        Location teleportLocation = DisableEnd.getPlayerTeleportLocation(p);
        if (teleportLocation == null) return;
        p.teleport(teleportLocation);
    }

    private static Player continueEvent(PlayerEvent e) {
        if (DisableEnd.CURRENT_BYPASS_CREATIVE_ADMINS && e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().isOp()) return null;
        World w = e.getPlayer().getWorld();
        if (!w.getEnvironment().equals(World.Environment.THE_END)) return null;
        return e.getPlayer();
    }

}
