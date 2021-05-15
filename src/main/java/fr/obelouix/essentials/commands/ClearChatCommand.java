package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClearChatCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (IPermission.test(player, "obelouix.clearchat")) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    for (int lineNumber = 0; lineNumber < 100; lineNumber++) {
                        if (lineNumber == 92 || lineNumber == 98) {
                            player.sendMessage(ChatColor.GOLD + "=====================================================");
                        } else if (lineNumber == 95) {
                            player.sendMessage(ChatColor.DARK_RED +
                                    "                      "
                                    + I18n.getInstance().sendTranslatedMessage(player, "command.clearchat.result"));
                        } else {
                            player.sendMessage("");
                        }
                    }

                }

            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Only a player can run this command");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
