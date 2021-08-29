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

public class OpenInventoryCommand extends BukkitCommand {

    public OpenInventoryCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (IPermission.test(player, "obelouix.commands.openinv")) {
                    player.openInventory(player.getInventory());
                }
            } else if (args.length == 1 && IPermission.test(player, "obelouix.commands.openinv.others")) {
                final Player target = Essentials.getInstance().getServer().getPlayer(args[0]);
                if (target != null) {
                    player.openInventory(target.getInventory());
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
        if (args.length == 1 && sender.hasPermission("obelouix.commands.openinv.others")) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getName().equals(sender.getName())) {
                    completion.add(p.getName());
                }
            }
            return completion;
        }
        return super.tabComplete(sender, alias, args);
    }
}
