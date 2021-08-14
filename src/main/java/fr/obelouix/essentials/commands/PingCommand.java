package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.IPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class PingCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                player.sendMessage(ChatColor.GRAY + MessageFormat.format(I18n.getInstance().sendTranslatedMessage(player, "command.ping.own"),
                        formatPingColorString(player) + ChatColor.GRAY));
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "It's useless to get your ping :p");
            }
        } else if (args.length == 1) {
            final Player target = Bukkit.getPlayer(args[0]);
            if (IPlayer.isOnline(args[0], sender)) {
                sender.sendMessage(ChatColor.GRAY + MessageFormat.format(I18n.getInstance().sendTranslatedMessage(sender, "command.ping.other"),
                        ChatColor.AQUA + target.getName() + ChatColor.GRAY, formatPingColorString(target) + ChatColor.GRAY));
            }
        } else {
            //CommandManager.getInstance().wrongCommandUsage(sender, command);
        }
        return true;
    }

    public String formatPingColorString(Player player) {
        final int playerPing = player.getPing();

        if (playerPing <= 50) {
            return ChatColor.DARK_GREEN + String.valueOf(playerPing);
        } else if (playerPing <= 100) {
            return ChatColor.GREEN + String.valueOf(playerPing);
        } else if (playerPing <= 150) {
            return ChatColor.YELLOW + String.valueOf(playerPing);
        } else {
            return ChatColor.DARK_RED + String.valueOf(playerPing);
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completion = new ArrayList<>();
        if (IPermission.test(sender, "obelouix.commands.ping.others") && args.length == 1) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getName().equals(sender.getName())) {
                    completion.add(player.getName());
                }
            }
        }
        return completion;
    }
}
