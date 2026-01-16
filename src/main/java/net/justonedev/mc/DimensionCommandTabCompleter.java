package net.justonedev.mc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class DimensionCommandTabCompleter implements TabCompleter {

    // Arguments are /command arg0 arg1 etc., but args are present and empty for the current one. So: /argslength here=1 here=2 here=3 4 ...

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.isOp() || args.length > 2) return List.of();
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("denyTeleport") || args[0].equalsIgnoreCase("kickPlayers")) {
                return List.of("true", "false", "status");
            }
            return List.of();
        }
        return List.of("true", "false", "status", "reload", "denyTeleport", "kickPlayers");
    }
}
