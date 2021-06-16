package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.IPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlyCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (IPermission.test(sender, "obelouix.commands.fly")) {
                final String target = args[0];
                if (IPlayer.isOnline(target, sender)) {
                    final boolean allowFlight = Objects.requireNonNull(Bukkit.getPlayer(target)).getAllowFlight();
                    Objects.requireNonNull(Bukkit.getPlayer(target)).setAllowFlight(!allowFlight);

                }
            }
        } else if (args.length == 0) {
            if (sender instanceof Player player) {
                final boolean allowFlight = player.getAllowFlight();
                player.setAllowFlight(!allowFlight);
                sendFlyMessage(sender, !allowFlight);
            }
        } else {
            CommandRegistrar.getInstance().wrongCommandUsage(sender, command);
        }
        return true;
    }

    private void sendFlyMessage(CommandSender sender, boolean allowFlight, String... player) {
        if (player.length == 0) {
            if (allowFlight)
                sender.sendMessage(ChatColor.GREEN + I18n.getInstance().sendTranslatedMessage(sender, "obelouix.commands.fly.enabled"));
            else
                sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender, "obelouix.commands.fly.disabled"));
        } else {
            if (allowFlight)
                sender.sendMessage(I18n.getInstance().sendTranslatedMessage(sender, "obelouix.commands.fly.target.enabled"));
            else
                sender.sendMessage(I18n.getInstance().sendTranslatedMessage(sender, "obelouix.commands.fly.target.disabled"));
        }
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> playerCollection = new ArrayList<>();
        if (args.length == 1) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                playerCollection.add(player.getName());
            }
        }
        return playerCollection;
    }
}
