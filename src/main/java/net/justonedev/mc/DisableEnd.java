package net.justonedev.mc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class DisableEnd extends JavaPlugin {

    public static DisableEnd instance;

    public static final boolean DEFAULT_END_DISABLED = false;
    public static final boolean DEFAULT_DENY_TELEPORT = true;
    public static final boolean DEFAULT_KICK_PLAYERS = true;
    public static final boolean DEFAULT_BYPASS_CREATIVE_ADMINS = true;
    public static final boolean DEFAULT_DO_PARTICLES = true;
    public static final boolean DEFAULT_DO_DENY_SOUNDS = true;

    public static final String DEFAULT_MSG_DENY_WORLD_TP = "&7Whew. It was all a dream.";
    public static final String DEFAULT_MSG_KICK_FROM_END = "&cThe End is currently disabled.";

    public static final boolean DEFAULT_NETHER_DISABLED = false;
    public static final boolean DEFAULT_NETHER_DENY_TELEPORT = true;
    public static final boolean DEFAULT_NETHER_KICK_PLAYERS = true;
    public static final boolean DEFAULT_ALLOW_NETHER_EXIT = true;

    public static final String DEFAULT_MSG_DENY_NETHER_TP = "&7Whew. It was all a dream.";
    public static final String DEFAULT_MSG_KICK_FROM_NETHER = "&cThe Nether is currently disabled.";

    public static boolean CURRENT_END_DISABLED;
    public static boolean CURRENT_DENY_TELEPORT;
    public static boolean CURRENT_KICK_PLAYERS;

    public static boolean CURRENT_NETHER_DISABLED;
    public static boolean CURRENT_DENY_NETHER_TELEPORT;
    public static boolean CURRENT_KICK_NETHER_PLAYERS;
    public static boolean CURRENT_ALLOW_NETHER_EXIT;

    public static boolean CURRENT_BYPASS_CREATIVE_ADMINS;
    public static boolean CURRENT_DO_PARTICLES;
    public static boolean CURRENT_DO_DENY_SOUNDS;

    public static String CURRENT_MSG_DENY_WORLD_TP;
    public static String CURRENT_MSG_KICK_FROM_END;

    public static String CURRENT_MSG_DENY_NETHER_TP;
    public static String CURRENT_MSG_KICK_FROM_NETHER;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Config.initialize();
        Cmd_DisableEnd cmdDisableEnd = new Cmd_DisableEnd();
        Objects.requireNonNull(getCommand("disableend")).setExecutor(cmdDisableEnd);
        Objects.requireNonNull(getCommand("disableend")).setTabCompleter(cmdDisableEnd);

        Cmd_DisableNether cmdDisableNether = new Cmd_DisableNether();
        Objects.requireNonNull(getCommand("disablenether")).setExecutor(cmdDisableNether);
        Objects.requireNonNull(getCommand("disablenether")).setTabCompleter(cmdDisableNether);

        Bukkit.getPluginManager().registerEvents(new EndEvents(), this);
        Bukkit.getPluginManager().registerEvents(new NetherEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static File getFolder() {
        return instance.getDataFolder();
    }
}
