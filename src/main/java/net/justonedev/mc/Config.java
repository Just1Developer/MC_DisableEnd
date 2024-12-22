package net.justonedev.mc;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final String KEY_END_DISABLED = "End disabled";
    private static final String KEY_DENY_WORLD_TP = "Deny End World Teleportation";
    private static final String KEY_KICK_PLAYERS = "Kick players out of the end";
    private static final String KEY_BYPASS = "Bypass for Admins in Creative Mode";
    private static final String KEY_PARTICLES = "Particles when denied placement of ender eye";
    private static final String KEY_SOUND = "Sound effect when denied placement of ender eye";
    private static final String KEY_MSG_DENY_WORLD_TP = "messages.Denied End Teleportation";
    private static final String KEY_MSG_KICK_FROM_END = "messages.Kicked from End";

    public static void initialize() {
        File f = new File(DisableEnd.getFolder(), "config.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        if (!f.exists()) {
            // Default Values are already set
            cfg.set(KEY_END_DISABLED, DisableEnd.DEFAULT_END_DISABLED);
            cfg.set(KEY_DENY_WORLD_TP, DisableEnd.DEFAULT_DENY_TELEPORT);
            cfg.set(KEY_KICK_PLAYERS, DisableEnd.DEFAULT_KICK_PLAYERS);
            cfg.set(KEY_BYPASS, DisableEnd.DEFAULT_KICK_PLAYERS);
            cfg.set(KEY_PARTICLES, DisableEnd.DEFAULT_DO_PARTICLES);
            cfg.set(KEY_SOUND, DisableEnd.DEFAULT_DO_DENY_SOUNDS);
            cfg.set(KEY_MSG_DENY_WORLD_TP, DisableEnd.DEFAULT_MSG_DENY_WORLD_TP);
            cfg.set(KEY_MSG_KICK_FROM_END, DisableEnd.DEFAULT_MSG_KICK_FROM_END);
            saveCfg(f, cfg);
            return;
        }

        Map<String, Object> updateThese = new HashMap<>();

        DisableEnd.CURRENT_END_DISABLED = getOrDefaultBoolean(KEY_END_DISABLED, cfg, updateThese, DisableEnd.DEFAULT_END_DISABLED);
        DisableEnd.CURRENT_DENY_TELEPORT = getOrDefaultBoolean(KEY_DENY_WORLD_TP, cfg, updateThese, DisableEnd.DEFAULT_DENY_TELEPORT);
        DisableEnd.CURRENT_KICK_PLAYERS = getOrDefaultBoolean(KEY_KICK_PLAYERS, cfg, updateThese, DisableEnd.DEFAULT_KICK_PLAYERS);
        DisableEnd.CURRENT_BYPASS_CREATIVE_ADMINS = getOrDefaultBoolean(KEY_BYPASS, cfg, updateThese, DisableEnd.DEFAULT_BYPASS_CREATIVE_ADMINS);

        DisableEnd.CURRENT_DO_PARTICLES = getOrDefaultBoolean(KEY_PARTICLES, cfg, updateThese, DisableEnd.DEFAULT_DO_PARTICLES);
        DisableEnd.CURRENT_DO_DENY_SOUNDS = getOrDefaultBoolean(KEY_SOUND, cfg, updateThese, DisableEnd.DEFAULT_DO_DENY_SOUNDS);

        DisableEnd.CURRENT_MSG_DENY_WORLD_TP = ChatColor.translateAlternateColorCodes('&', getOrDefaultString(KEY_MSG_DENY_WORLD_TP, cfg, updateThese, DisableEnd.DEFAULT_MSG_DENY_WORLD_TP));
        DisableEnd.CURRENT_MSG_KICK_FROM_END = ChatColor.translateAlternateColorCodes('&', getOrDefaultString(KEY_MSG_KICK_FROM_END, cfg, updateThese, DisableEnd.DEFAULT_MSG_KICK_FROM_END));

        if (updateThese.isEmpty()) return;
        cfg = YamlConfiguration.loadConfiguration(f);	// Reload config
        for (Map.Entry<String, Object> entry : updateThese.entrySet()) {
            cfg.set(entry.getKey(), entry.getValue());
        }
        saveCfg(f, cfg);
    }

    private static boolean getOrDefaultBoolean(String key, YamlConfiguration cfg, Map<String, Object> updateThese, boolean defaultValue)
    {
        if (cfg.isSet(key)) return cfg.getBoolean(key);
        updateThese.put(key, defaultValue);
        return defaultValue;
    }

    private static String getOrDefaultString(String key, YamlConfiguration cfg, Map<String, Object> updateThese, String defaultValue)
    {
        if (cfg.isSet(key)) {
            String value = cfg.getString(key);
            return value == null ? defaultValue : value;
        }
        updateThese.put(key, defaultValue);
        return defaultValue;
    }

    private static void saveCfg(File f, YamlConfiguration cfg) {
        try {
            cfg.save(f);
        } catch (IOException ignored) {}
    }

    public static void updateDisableEnd() {
        File f = new File(DisableEnd.getFolder(), "config.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        cfg.set(KEY_END_DISABLED, DisableEnd.CURRENT_END_DISABLED);
        saveCfg(f, cfg);
    }

    public static void updateDenyWorldTeleport() {
        File f = new File(DisableEnd.getFolder(), "config.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        cfg.set(KEY_DENY_WORLD_TP, DisableEnd.CURRENT_DENY_TELEPORT);
        saveCfg(f, cfg);
    }

    public static void updateKickPlayers() {
        File f = new File(DisableEnd.getFolder(), "config.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        cfg.set(KEY_KICK_PLAYERS, DisableEnd.CURRENT_KICK_PLAYERS);
        saveCfg(f, cfg);
    }

}
