package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeCommand implements CommandExecutor, TabCompleter {

    private final List<String> times = Arrays.asList("morning", "midday", "noon", "midnight", "day", "night");
    private final DecimalFormat format = new DecimalFormat("00");
    private long worldHour;
    private long worldMinute;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (IPermission.test(sender, "obelouix.time")) {

            if (args.length == 0) {
                if (sender instanceof Player player) {

                    worldHour = player.getWorld().getTime() / 1000 + 6;
                    worldMinute = ((player.getWorld().getTime()) % 1000) * 60 / 1000;

                    if (worldHour > 23) {
                        worldHour = worldHour - 24;
                    }

                    player.sendMessage(MessageFormat.format(ChatColor.GOLD + I18n.getInstance().sendTranslatedMessage(player, "current_world_time"),
                            ChatColor.RED + format.format(worldHour) + "h" + format.format(worldMinute) + ChatColor.GOLD,
                            ChatColor.RED + ((Player) sender).getWorld().getName() + ChatColor.GOLD));
                } else {
                    for (final World world : Bukkit.getWorlds()) {
                        worldHour = world.getTime() / 1000 + 6;
                        worldMinute = ((world.getTime()) % 1000) * 60 / 1000;

                        if (worldHour > 23) {
                            worldHour = worldHour - 24;
                        }

                        sender.sendMessage(MessageFormat.format(I18n.getInstance().sendTranslatedMessage(sender, "current_world_time"),
                                format.format(worldHour) + "h" + format.format(worldMinute), world.getName()));
                    }
                }
            } else if (args.length == 1) {
                final Player player = (Player) sender;
                if (args[0].equalsIgnoreCase("day")) {
                    if (IPermission.test(player, "obelouix.time.day")) {
                        player.getWorld().setTime(0);
                        sendPlayerTimeMessage(player, 0);
                    }
                } else if (args[0].equalsIgnoreCase("morning")) {
                    if (IPermission.test(player, "obelouix.time.morning")) {
                        player.getWorld().setTime(2000);
                        sendPlayerTimeMessage(player, 2000);
                    }
                } else if (args[0].equalsIgnoreCase("midday")) {
                    if (IPermission.test(player, "obelouix.time.midday")) {
                        player.getWorld().setTime(6000);
                        sendPlayerTimeMessage(player, 6000);
                    }
                } else if (args[0].equalsIgnoreCase("noon")) {
                    if (IPermission.test(player, "obelouix.time.noon")) {
                        player.getWorld().setTime(9000);
                        sendPlayerTimeMessage(player, 9000);
                    }
                } else if (args[0].equalsIgnoreCase("night")) {
                    if (IPermission.test(player, "obelouix.time.night")) {
                        player.getWorld().setTime(13188);
                        sendPlayerTimeMessage(player, 13188);
                    }
                } else if (args[0].equalsIgnoreCase("midnight")) {
                    if (IPermission.test(player, "obelouix.time.midnight")) {
                        player.getWorld().setTime(18000);
                        sendPlayerTimeMessage(player, 18000);
                    }
                } else {
                    CommandRegistrar.getInstance().wrongCommandUsage(player, command);
                }
            }
        }
        return true;
    }

    protected void sendPlayerTimeMessage(CommandSender sender, int time) {
        final Player player = (Player) sender;
        worldHour = time / 1000 + 6;
        worldMinute = (time % 1000) * 60 / 1000;

        if (worldHour > 23) {
            worldHour = worldHour - 24;
        }

        sender.sendMessage(ChatColor.GOLD + MessageFormat.format(I18n.getInstance().sendTranslatedMessage(player, "command.time.set"),
                ChatColor.RED + player.getWorld().getName() + ChatColor.GOLD,
                ChatColor.RED + format.format(worldHour) + "h" + format.format(worldMinute) + ChatColor.GOLD
        ));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completion = new ArrayList<>();
        if (args.length == 1) {
            for (final String time : times) {
                if (sender.hasPermission("obelouix.time." + time)) {
                    completion.add(time);
                }
            }
        }
        return completion;
    }
}
