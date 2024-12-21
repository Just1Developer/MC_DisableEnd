package net.justonedev.mc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cmd_DisableEnd implements CommandExecutor, TabCompleter {

    private static final Pattern STATUS_REGEX = Pattern.compile("^(true|false|status)$");
    private static final Pattern SUBCOMMANDS_REGEX = Pattern.compile("^(denyteleport|kickplayers)$");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.isOp()) {
            String msg = Bukkit.spigot().getConfig().getString("messages.unknown-command");
            if (msg == null) msg = "§4You are not allowed to use this command!";
            commandSender.sendMessage(msg);
            return true;
        }

        if (args.length > 2) {
            sendSyntax(commandSender);
            return false;
        }

        if (args.length == 0) {
            sendSyntax(commandSender);
            return false;
        }

        if (args.length == 2) {
            Matcher sub = SUBCOMMANDS_REGEX.matcher(args[0].toLowerCase());
            Matcher val = STATUS_REGEX.matcher(args[1].toLowerCase());
            if (!sub.matches() || !val.matches()) {
                commandSender.sendMessage("§c/disableend <denyTeleport | kickPlayers> <true | false | status>");
                return false;
            }
            if (sub.group(1).equalsIgnoreCase("denyTeleport")) {
                if (val.group(1).equalsIgnoreCase("status")) {
                    commandSender.sendMessage("§eDeny World Teleport is currently set to %s".formatted(getBoolean(DisableEnd.CURRENT_DENY_TELEPORT)));
                } else {
                    boolean value = Boolean.parseBoolean(val.group(1));
                    if (value == DisableEnd.CURRENT_DENY_TELEPORT) {
                        commandSender.sendMessage("§eDeny World Teleport is already set to %s".formatted(getBoolean(DisableEnd.CURRENT_DENY_TELEPORT)));
                    } else {
                        DisableEnd.CURRENT_DENY_TELEPORT = value;
                        Config.updateDenyWorldTeleport();
                        commandSender.sendMessage("§eSet Deny World Teleport to %s".formatted(getBoolean(DisableEnd.CURRENT_DENY_TELEPORT)));
                    }
                }
            } else if (sub.group(1).equalsIgnoreCase("kickPlayers")) {
                if (val.group(1).equalsIgnoreCase("status")) {
                    commandSender.sendMessage("§eKick Players from End is currently set to %s".formatted(getBoolean(DisableEnd.CURRENT_KICK_PLAYERS)));
                } else {
                    boolean value = Boolean.parseBoolean(val.group(1));
                    if (value == DisableEnd.CURRENT_KICK_PLAYERS) {
                        commandSender.sendMessage("§eKick Players from End is already set to %s".formatted(getBoolean(DisableEnd.CURRENT_KICK_PLAYERS)));
                    } else {
                        DisableEnd.CURRENT_KICK_PLAYERS = value;
                        Config.updateKickPlayers();
                        commandSender.sendMessage("§eSet Kick Players from End to %s".formatted(getBoolean(DisableEnd.CURRENT_KICK_PLAYERS)));
                    }
                }
            }
            return true;
        }

        Matcher val = STATUS_REGEX.matcher(args[0].toLowerCase());
        if (!val.matches()) {
            sendSyntax(commandSender);
            return false;
        }
        if (val.group(1).equalsIgnoreCase("status")) {
            commandSender.sendMessage("§eThe End Dimension is currently %s".formatted(getBooleanStringify(!DisableEnd.CURRENT_END_DISABLED)));
        } else {
            boolean value = Boolean.parseBoolean(val.group(1));
            if (value == DisableEnd.CURRENT_END_DISABLED) {
                commandSender.sendMessage("§eThe End Dimension is already %s".formatted(getBooleanStringify(!DisableEnd.CURRENT_END_DISABLED)));
            } else {
                DisableEnd.CURRENT_END_DISABLED = value;
                Config.updateDisableEnd();
                commandSender.sendMessage((DisableEnd.CURRENT_END_DISABLED ? "§cDisabled" : "§aEnabled") + "§e the End Dimension");
            }
        }

        return true;
    }

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
        return List.of("true", "false", "status", "denyTeleport", "kickPlayers");
    }

    private static void sendSyntax(CommandSender commandSender) {
        commandSender.sendMessage("§e/disableend <true | false | status> or /disableend <denyTeleport | kickPlayers> <true | false | status>");
    }

    private static String getBoolean(boolean bool) {
        return bool ? "§atrue" : "§cfalse";
    }

    private static String getBooleanStringify(boolean bool) {
        return bool ? "§aenabled" : "§cdisabled";
    }
}
