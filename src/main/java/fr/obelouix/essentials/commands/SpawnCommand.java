package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.IPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnCommand implements TabExecutor {

    private final Essentials plugin = Essentials.getInstance();
    private final I18n i18n = I18n.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final World overworld = Bukkit.getWorlds().get(0);
        if (args.length < 2) {
            if (args.length == 0) {
                if (IPermission.test(sender, "obelouix.commands.spawn")) {
                    if (sender instanceof Player player) {
                        player.teleport(overworld.getSpawnLocation());
                        player.sendMessage(ChatColor.GOLD + i18n.getTranslation(sender, "obelouix.commands.spawn"));
                    } else {
                        plugin.getLOGGER().info("You must be a player to teleport yourself to spawn !");
                        //CommandManager.getInstance().wrongCommandUsage(sender, command);
                    }
                }
            }
            if (args.length == 1) {
                if (IPermission.test(sender, "obelouix.commands.spawn.others")) {
                    if (IPlayer.isOnline(args[0], sender)) {
                        if (args[0].contains("*")) {
                            for (final Player player : Bukkit.getOnlinePlayers()) {
                                player.teleport(overworld.getSpawnLocation());
                                if (sender instanceof Player player1) {
                                    player1.sendMessage(ChatColor.GOLD + i18n.getTranslation(sender, "obelouix.commands.spawn.everyone"));
                                }
                                plugin.getLOGGER().info(sender.getName() + " teleported " + player.getName() + " to spawn");
                            }
                        } else {
                            final Player target = Bukkit.getPlayer(args[0]);
                            Objects.requireNonNull(target).teleport(overworld.getSpawnLocation());
                            if (sender instanceof Player player) {
                                player.sendMessage(MessageFormat.format(ChatColor.GOLD + i18n.getTranslation(sender, "obelouix.commands.spawn.others"),
                                        ChatColor.RED + target.getName() + ChatColor.GOLD));
                            }
                            plugin.getLOGGER().info(sender.getName() + " teleported " + target.getName() + " to spawn");
                        }
                    }
                }
            }
        } else {
            if (sender.hasPermission("obelouix.commands.spawn") || sender.hasPermission("obelouix.commands.spawn.others")) {
                //CommandManager.getInstance().wrongCommandUsage(sender, command);
            } else
                sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().getTranslation(sender, "no_permission"));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender.hasPermission("obelouix.commands.spawn.other") && args.length == 1) {
            final List<String> playerCollection = new ArrayList<>();
            playerCollection.add("*");
            for (final Player player : Bukkit.getOnlinePlayers()) {
                playerCollection.add(player.getName());
            }
            return playerCollection;
        }
        return null;
    }
}
