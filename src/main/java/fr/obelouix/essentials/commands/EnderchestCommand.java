package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EnderchestCommand extends BukkitCommand {

    protected EnderchestCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0 && IPermission.test(player, "obelouix.commands.enderchest")) {
                player.openInventory(player.getEnderChest());
            } else if (args.length == 1 && IPermission.test(player, "obelouix.commands.enderchest.others")) {
                final Player target = Essentials.getInstance().getServer().getPlayer(args[0]);
                if (target != null) {
                    player.openInventory(target.getEnderChest());
                } else {
                    player.sendMessage(ChatColor.GOLD + args[0] + " "
                            + ChatColor.DARK_RED + I18n.getInstance().getTranslation(player, "player_not_online"));
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        final List<String> completion = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("obelouix.commands.enderchest.others")) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getName().equals(sender.getName())) {
                    completion.add(player.getName());
                }
            }
            return completion;
        }
        return super.tabComplete(sender, alias, args);
    }
}
