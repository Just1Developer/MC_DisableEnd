package net.justonedev.mc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class DisableEnd extends JavaPlugin {

    private static DisableEnd singleton;

    public static final boolean DEFAULT_END_DISABLED = false;
    public static final boolean DEFAULT_DENY_TELEPORT = true;
    public static final boolean DEFAULT_KICK_PLAYERS = true;
    public static final boolean DEFAULT_BYPASS_CREATIVE_ADMINS = true;
    public static final boolean DEFAULT_DO_PARTICLES = true;
    public static final boolean DEFAULT_DO_DENY_SOUNDS = true;

    public static final String DEFAULT_MSG_DENY_WORLD_TP = "&7Whew. It was all a dream.";
    public static final String DEFAULT_MSG_KICK_FROM_END = "&cThe End is currently disabled.";

    public static boolean CURRENT_END_DISABLED;
    public static boolean CURRENT_DENY_TELEPORT;
    public static boolean CURRENT_KICK_PLAYERS;

    public static boolean CURRENT_BYPASS_CREATIVE_ADMINS;
    public static boolean CURRENT_DO_PARTICLES;
    public static boolean CURRENT_DO_DENY_SOUNDS;

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
        Bukkit.getPluginManager().registerEvents(new EndEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static File getFolder() {
        return singleton.getDataFolder();
    }
}
