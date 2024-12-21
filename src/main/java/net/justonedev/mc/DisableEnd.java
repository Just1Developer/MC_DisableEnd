package net.justonedev.mc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class DisableEnd extends JavaPlugin implements CommandExecutor {

    private static DisableEnd singleton;

    public static final boolean DEFAULT_END_DISABLED = false;
    public static final boolean DEFAULT_DENY_TELEPORT = true;
    public static final boolean DEFAULT_KICK_PLAYERS = true;

    public static final String DEFAULT_MSG_DENY_WORLD_TP = "&7Whew. It was all a dream.";
    public static final String DEFAULT_MSG_KICK_FROM_END = "&cThe End is currently disabled.";

    public static boolean CURRENT_END_DISABLED;
    public static boolean CURRENT_DENY_TELEPORT;
    public static boolean CURRENT_KICK_PLAYERS;

    public static String CURRENT_MSG_DENY_WORLD_TP;
    public static String CURRENT_MSG_KICK_FROM_END;

    @Override
    public void onEnable() {
        // Plugin startup logic
        singleton = this;
        Config.initialize();
        Cmd_DisableEnd cmd = new Cmd_DisableEnd();
        Objects.requireNonNull(getCommand("disableend")).setExecutor(cmd);
        Objects.requireNonNull(getCommand("disableend")).setTabCompleter(cmd);
        Objects.requireNonNull(getCommand("test")).setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new EndEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static File getFolder() {
        return singleton.getDataFolder();
    }

    Sound[] sounds = { Sound.AMBIENT_CAVE, Sound.ENTITY_WARDEN_TENDRIL_CLICKS, Sound.ENTITY_ELDER_GUARDIAN_CURSE, Sound.ITEM_ARMOR_EQUIP_NETHERITE, Sound.ITEM_SHIELD_BREAK, Sound.ITEM_TOTEM_USE};
    Particle[] particles = { Particle.ASH, Particle.CAMPFIRE_SIGNAL_SMOKE, Particle.DUST, Particle.SMOKE, Particle.CLOUD, Particle.SMALL_GUST, Particle.SMALL_FLAME };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return true;

        /*int arg = Integer.parseInt(args[0]) % sounds.length;
        Sound sound = sounds[arg];
        sender.sendMessage("Playing sound §5" + sound.toString());
        ((Player) sender).playSound(((Player) sender).getLocation(), sound, 1, 1);*/


        int arg = Integer.parseInt(args[0]) % particles.length;
        Particle particle = particles[arg];
        sender.sendMessage("Playing particle §5" + particle.toString());
        Location loc = ((Player) sender).getLocation();
        if (loc.getWorld() != null) loc.getWorld().spawnParticle(particle, loc, 10);

        return true;
    }
}
