package net.justonedev.mc;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class EndEvents implements Listener {

    @EventHandler
    public void onPlaceEndereye(PlayerInteractEvent e) {
        if (!DisableEnd.CURRENT_END_DISABLED) return;
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().isOp()) return;
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem() == null) return;
        if (e.getItem().getType() != Material.ENDER_EYE) return;
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getType() != Material.END_PORTAL_FRAME) return;

        Player p = e.getPlayer();
        p.playSound(p, Sound.ITEM_TOTEM_USE, 1, 1);
        Location loc = e.getClickedBlock().getLocation().add(0, 0.35, 0);
        if (loc.getWorld() != null) loc.getWorld().spawnParticle(Particle.SMOKE, loc, 65);
        e.setCancelled(true);
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        if (!DisableEnd.CURRENT_END_DISABLED) return;
        if (!DisableEnd.CURRENT_DENY_TELEPORT) return;
        World w = e.getPlayer().getWorld();
        if (!w.getEnvironment().equals(World.Environment.THE_END)) return;
        Player p = e.getPlayer();
        if (!DisableEnd.CURRENT_MSG_DENY_WORLD_TP.isBlank()) p.sendMessage(DisableEnd.CURRENT_MSG_DENY_WORLD_TP);
        World defaultWorld = Bukkit.getWorlds().getFirst();
        if (defaultWorld == null && p.getRespawnLocation() == null) return;
        p.teleport(p.getRespawnLocation() == null ? Objects.requireNonNull(defaultWorld).getSpawnLocation() : p.getRespawnLocation());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!DisableEnd.CURRENT_END_DISABLED) return;
        if (!DisableEnd.CURRENT_KICK_PLAYERS) return;
        World w = e.getPlayer().getWorld();
        if (!w.getEnvironment().equals(World.Environment.THE_END)) return;
        Player p = e.getPlayer();
        if (!DisableEnd.CURRENT_MSG_KICK_FROM_END.isBlank()) p.sendMessage(DisableEnd.CURRENT_MSG_KICK_FROM_END);
        World defaultWorld = Bukkit.getWorlds().getFirst();
        if (defaultWorld == null && p.getRespawnLocation() == null) return;
        p.teleport(p.getRespawnLocation() == null ? Objects.requireNonNull(defaultWorld).getSpawnLocation() : p.getRespawnLocation());
    }

}